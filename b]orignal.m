clear all; close all; dbstop error

global MyContext

global ABCD;
ABCD.flagPause=0;

% PART 1
MyFile = 'IMU_data.mat';  
load(MyFile) ;
load('NavMap.mat');
vehicle.x = 0; vehicle.y = 0; vehicle.h = 0;

times = double(IMU.times)/10000;
% IMU.times is an array that contains the timestamps of the measurements.
% Each sample has an associated timestamp (sample time)

times = times - times(1) ;  % just, in order to refer to t0=0 (not necessary).

% IMU.Gyros(1,:) is Wx  (local Roll rate)
% IMU.Gyros(2,:) is Wy  (local Pitch rate)
% IMU.Gyros(3,:) is Wz  (local Yaw rate)
% All measurements are expressed in radians/second

Wx = double(IMU.DATAf(4,:));
Wy = double(IMU.DATAf(5,:));
Wz = double(IMU.DATAf(6,:));

k = 180/pi; % constant, useful for converting radian to degrees.
calibrationN = 3000; % number of elements that are within 15 seconds

bias(1) = mean(Wx(1:calibrationN));
bias(2) = mean(Wy(1:calibrationN));
bias(3) = mean(Wz(1:calibrationN));

unbiasWz = -(Wz - bias(3));

UnbiasAtt = zeros(length(times),3);
for n = 1:length(times)
    
    if n == 1
      dt = 0.005;
    else
      dt = times(n) - times(n-1);
    end
    
    if n == 1
        curAtt = [0,0,0];
    else
        curAtt = newAtt;
    end
    
    newAtt = EulerIntegrate(Wx(n) - bias(1), Wy(n) - bias(2), -(Wz(n) - bias(3)), dt, curAtt); 
    UnbiasAtt(n,:) = newAtt;
    
end

figure(2); clf(); hold on;

subplot(3,1,1), plot(times,UnbiasAtt(:,1)*k);
title('Estimated Roll');
xlabel('Time (seconds)');
ylabel('Angle (degrees)');
grid on;

subplot(3,1,2), plot(times,UnbiasAtt(:,2)*k);
title('Estimated Pitch');
xlabel('Time (seconds)');
ylabel('Angle (degrees)');
grid on;

subplot(3,1,3), plot(times,UnbiasAtt(:,3)*k);
title('Estimated Yaw');
xlabel('Time (seconds)');
ylabel('Angle (degrees)');
grid on;


% PART 2
file = 'DepthData01.mat';   
load(file);        % here we load the file, specified by the caller.

L = CR.N; % get number of data points  
laserTimes = double(CR.H(1,:) - CR.H(1))/10000;
% load API 
API = IniAPIGetPointCloudFromDepth();
% This is a small API, for corverting depth data into 3D points. 

% The values are expressed for images 640x480, but internally adapted
% to our resolution of 160x120 

API.SetProjectionConstants(1,340,0.001848, 240,0.001865); 
fprintf('(using API ver=[%.1f])\n',API.Info.version);
global flag;

flag = 0;
% initialise some variables
prevTrMatrix = eye(3);
quality = 75;
thetaY = 0;
thetaX = 0;
trMatrix = eye(3);
pitchData = zeros(1,L);
rollData = zeros(1,L);

MyContext.x1 = 0 ;MyContext.x2 = 200 ;
MyContext.y1 = 0 ;MyContext.y2 = 200 ;
MyContext.Nx = 40 ;
MyContext.Ny = 40 ;

DefineOG() ;
% .....................
% figure(5) ; clf ; plot(0,0) ;
% axis([MyContext.x1,MyContext.x2,MyContext.y1,MyContext.y2]) ;

%disp('input (mouse click) 10 points in figure 1') ;
%pp =ginput(10) ;
figure(6); clf ; 
MyContext.handle = imagesc(MyContext.M) ; 
colormap hot ;
set(gca,'ydir','normal','xdir','normal') ;
%PopulateOG(Landmarks.xy(1,:),Landmarks.xy(2,:)) ;
%set(MyContext.handle, 'cdata', MyContext.M);



for i=1:L             

    if i == 358
        pause; 
    end
    Depth = CR.R(:,:,i);  %get a copy of depth image #i;

    % Call API method for obtaining its associated 3D points.
    % scale factor =0.1, so the resulting 3D points are expressed in Cm.
    [xx,yy,zz]=API.ConvertDepthsTo3DPoints(Depth,0.1) ; 

    % choose region of interest
    ROI.X = 50:110;
    ROI.Y = 70:120;

    xxROI = xx(ROI.Y,ROI.X); yyROI = yy(ROI.Y,ROI.X); zzROI = zz(ROI.Y,ROI.X);
    
    % fit plane using least square method
    Avec = [reshape(xxROI,numel(xxROI),1) reshape(yyROI,numel(yyROI),1) ones(numel(yyROI),1)];
    Bvec = reshape(zzROI,numel(zzROI),1);

    % solve matrix
    result = (Avec' * Avec) \ Avec' * Bvec;
    normalVec = [-result(1) -result(2) 1];

    % normalise normal vector
    nnVec = normalVec/norm(normalVec);

    % hat vector to check fit
    Bvechat = Avec * result;

    residual = Bvec - Bvechat;
    rsquare = sum(residual.^2);

    if(rsquare < quality && rsquare > 0)
        % solving equations analytically
        thetaY = asin(-nnVec(1));
        thetaX = asin(nnVec(2)/cos(thetaY));
        thetaZ = 0;

        sx = sin(thetaX);
        cx = cos(thetaX);
        sy = sin(thetaY);
        cy = cos(thetaY);
        sz = sin(thetaZ);
        cz = cos(thetaZ);

        % generate transformation matrix
        trMatrix = [cy*cz -cx*sz+sx*sy*cz  sx*sz+cx*sy*cz;
                    cy*sz  cx*cz+sx*sy*sz -sx*cz+cx*sy*sz;
                    -sy    sx*cy           cx*cy];

        xx = reshape(xx,1,numel(xx));
        yy = reshape(yy,1,numel(yy));
        zz = reshape(zz,1,numel(zz));

        trPlot = trMatrix*[xx;yy;zz];

        % store the good fitting trans. matrix
        prevTrMatrix = trMatrix;

    else
        % if the plane fit is bad, use previous trans. matrix
        xx = reshape(xx,1,numel(xx));
        yy = reshape(yy,1,numel(yy));
        zz = reshape(zz,1,numel(zz));

        trPlot = prevTrMatrix*[xx;yy;zz];
    end

    xx = trPlot(1,:);
    yy = trPlot(2,:);
    zz = trPlot(3,:);
    
    xx = reshape(xx,120,160);
    yy = reshape(yy,120,160);
    zz = reshape(zz,120,160);
    
    if(~flag)
        iniPitch = rad2deg(thetaY);
        iniRoll = rad2deg(thetaX);
        fprintf('Initial Pitch = %f deg, Initial Roll = %f deg \n', iniPitch, iniRoll);
        pause(1);
    else

    % display the camera's estimated pitch and roll
    pitchData(i) = rad2deg(thetaY) - iniPitch;
    rollData(i) = rad2deg(thetaX) - iniRoll;
    %fprintf('Pitch = %f deg, Roll = %f deg \n', pitchData(i), rollData(i));
    
    end
    % update graphics to match transformed 3d cloud
    
    % get rid of faulty points
    xx(xx==0) = NaN;
    yy(yy==0) = NaN;
    zz(zz==0) = NaN;
    zz = zz + 30; % floor is at 0
    
    %%% Part 4
    % circular area in front of vehicle
    trRange = hypot(xx, yy);
    
    % risky point checking
    risk = (trRange < 60) & (zz > 10) & (zz < 50);
    riskxx = xx(risk);
    riskyy = yy(risk);
    riskzz = zz(risk);
    
    %%% Part 3
    % simulate a laser scan
    ii = find(zz>15 & zz<16);
    xxL = xx(ii);
    yyL = yy(ii);
    zzL = zz(ii);
    
    separation = diff(xxL);
    
    poleCenters = [NaN;NaN];
    start = 1;
    poleN = 0;
    
    for n = 1:numel(xxL)-1
        
        if abs(xxL(n) - xxL(n+1)) > 10 || n == numel(xxL)-1 % first check if the current point is 10cm from next, therefore making it the last point of the cluster
           firstPointX = xxL(start);
           firstPointY = yyL(start);
           lastPointX = xxL(n);
           lastPointY = yyL(n);
           
           size = hypot(firstPointX - lastPointX,firstPointY - lastPointY);
           
           if (size >= 0.5 && size <= 4) && start == 1
               
               poleN = poleN + 1;
               poleCenters(1,poleN) = (firstPointX + lastPointX)./2; % x coord
               poleCenters(2,poleN) = (firstPointY + lastPointY)./2; % y coord                           
               start = n + 1;

           elseif (size >= 0.5 && size <= 4) && (separation(start-1) < 0 && separation(n) > 0) % check if the cluster is within the size range to be a pole
                
               poleN = poleN + 1;
               poleCenters(1,poleN) = (firstPointX + lastPointX)./2; % x coord
               poleCenters(2,poleN) = (firstPointY + lastPointY)./2; % y coord                           
               start = n + 1;
               
           else
               start = n + 1;
           end
        end
    end
    
    fprintf('No. of Poles visible: %d\n', poleN);
    for ii = 1:poleN
        %fprintf('Coord. of Pole %d: X = %.3f, Y = %.3f\n', ii, poleCenters(1,ii), poleCenters(2,ii));
    end
       
    if flag  
        
        % Global
        gPoleCenters = GlobalTransform(poleCenters,vehicle);
        set(hGPoles, 'xdata', gPoleCenters(1,:), 'ydata', gPoleCenters(2,:), 'color', 'b');


        AOOI = FindAOOI(gPoleCenters, Landmarks);
        if ~isempty(AOOI.localIndex) % don't do anything if no AOOIs seen
            if length(AOOI.globalIndex) == 1 && AOOI.globalIndex ~= 0
                dx = Landmarks.xy(1,AOOI.globalIndex) - gPoleCenters(1,AOOI.localIndex); 
                dy = Landmarks.xy(2,AOOI.globalIndex) - gPoleCenters(2,AOOI.localIndex);
                vehicle.x = vehicle.x + dx;
                vehicle.y = vehicle.y + dy;
                vehicle.h = initH + UnbiasAtt(i*54,3);
                gPoleCenters(1,:) = gPoleCenters(1,:) + dx;
                gPoleCenters(2,:) = gPoleCenters(2,:) + dy;
            end
            
            if length(AOOI.globalIndex) > 1
                
                ogPC = gPoleCenters; % save original untransformed pole center positions
                initGuess = [0,0,0];
                %options = optimset('Display', 'final');
           
                % error = Adjustment(AOOI, gPC, LMs, veh, xshift, yshift, angle)
                tic;
                sol = fminsearch(@(guess) Adjustment(AOOI, poleCenters, Landmarks, vehicle, guess(1), guess(2), guess(3)), initGuess);%, options);
                error1 = Adjustment(AOOI, poleCenters, Landmarks, vehicle, sol(1), sol(2), sol(3));
                %disp(error);
                [xshift, yshift] = SecondAdjust(AOOI, poleCenters, Landmarks, vehicle, sol(1), sol(2), sol(3));
                sol(1) = sol(1) + xshift;
                sol(2) = sol(2) + yshift;
                error2 = Adjustment(AOOI, poleCenters, Landmarks, vehicle, sol(1), sol(2), sol(3));
                %disp(error);
                toc;
                
                vehicle.x = vehicle.x + sol(1);
                vehicle.y = vehicle.y + sol(2);
                vehicle.h = vehicle.h + sol(3);
                gPoleCenters = GlobalTransform(poleCenters,vehicle);
%                 gPoleCenters(1,:) = gPoleCenters(1,:) + sol(1);
%                 gPoleCenters(2,:) = gPoleCenters(2,:) + sol(2);
            end
            error1Hist(end+1) = error1;
            error2Hist(end+1) = error2;
            
%             ray1 = linspace(0,200,101);
%             ray1 = [ray1;zeros(1,length(ray1))];
%             ray2 = ray1;
%             angView = deg2rad(60);
%             ray1 = RayTransform(ray1, vehicle, angView/2);
%             ray2 = RayTransform(ray2, vehicle, -angView/2);

            ray = [linspace(0,200,101); zeros(1,length(linspace(0,200,101)))];
            angView = deg2rad(60);
            angView = linspace(-angView/2,angView/2,60);
            rays = RayTransform(ray, vehicle, angView);
            
            rayIndex = PopulateRay(rays);
            PopulatePC(gPoleCenters, rayIndex);
            PopulateVeh(vehicle);
            
            set(MyContext.handle, 'cdata', MyContext.M);
     
            vehicleHist(:,end+1) = [vehicle.x; vehicle.y; vehicle.h];
            set(hGPoles, 'xdata', gPoleCenters(1,:), 'ydata', gPoleCenters(2,:), 'color', 'r');
            set(hVehicle, 'xdata', vehicle.x, 'ydata', vehicle.y, 'udata', initD*cos(vehicle.h), 'vdata', initD*sin(vehicle.h));
            set(hPath, 'xdata', vehicleHist(1,:), 'ydata', vehicleHist(2,:));
            set(hError1, 'ydata', error1Hist(:), 'xdata', 1:length(error1Hist));
            set(hError2, 'ydata', error2Hist(:), 'xdata', 1:length(error2Hist));
        end
        
        %disp(vehicle);
        
        % Plot
        set(h1,'cdata',Depth);
        set(h2,'xdata',xx(1:end),'ydata',yy(1:end),'zdata',zz(1:end));
        set(hRisk, 'xdata',riskxx,'ydata',riskyy,'zdata',riskzz);
        set(hLaser,'xdata',xxL,'ydata',yyL,'zdata',zzL);
        s = sprintf('Depth at time [%.3f] secs',laserTimes(i));
        set(titleHandle,'string',s);

        dataPts = times<laserTimes(i);
        set(hRoll,'xdata',times(dataPts),'ydata',UnbiasAtt(dataPts,1)*k);
        set(hRoll2,'xdata',laserTimes(1:i),'ydata',rollData(1:i));
        set(hPitch,'xdata',times(dataPts),'ydata',UnbiasAtt(dataPts,2)*k);
        set(hPitch2,'xdata',laserTimes(1:i),'ydata',pitchData(1:i));
        set(hYaw,'xdata',times(dataPts),'ydata',UnbiasAtt(dataPts,3)*k);

        set(h2dLaser, 'xdata', xxL, 'ydata', yyL);
        set(hPoles, 'xdata', poleCenters(1,:), 'ydata', poleCenters(2,:));
        s = sprintf('Simulated laser scan at time [%.3f] secs',laserTimes(i));
        set(laserTitleHandle,'string',s);

    else   %First time: we create Matlab graphical objects
        flag=1;
        figure(1) ; clf() ;  

        subplot(211) ; h1 = imagesc(Depth,[100,2000]); % displayed with scaled colours
        % image, scalling color for values in range from 100mm to 2000.

        set(gca(),'xdir','reverse'); % gca = get current axis   
        % this way, that image looks better (for human brains..)
        colormap gray ; 
        zoom on;   %title('Depth, shown as an image');
        
        titleHandle = title('');
        
        uicontrol('Style','pushbutton','String','Pause/Cont.','Position',[10,1,80,20],'Callback',{@MyCallBackA,1});
   
        
        subplot(212);
        h2 = plot3(xx(1:end),yy(1:end),zz(1:end),'.b','markersize',0.5); hold on;
        hRisk = plot3(0,0,0,'.r','markersize',1);
        hLaser = plot3(0,0,0,'.g','markersize',1);
        
        axis([0,200,-125,125,-40,60]); 
        rotate3d on; grid on;
        xlabel('X (cm)'); ylabel('Y (cm)'); zlabel('Z (cm)');
        title('3D Points Cloud (view from camera)');
        hold on;
        
        figure(2); clf(); hold on;

        subplot(3,1,1), hRoll = plot(times,UnbiasAtt(:,1)*k); hold on;
        subplot(3,1,1), hRoll2 = plot(0,0,'r-');
        title('Estimated Roll');
        xlabel('Time (seconds)');
        ylabel('Angle (degrees)');
        legend({'Gyro Data','Floor Data'});
        grid on;

        subplot(3,1,2), hPitch = plot(times,UnbiasAtt(:,2)*k); hold on;
        subplot(3,1,2), hPitch2 = plot(0,0,'r-');
        title('Estimated Pitch');
        xlabel('Time (seconds)');
        ylabel('Angle (degrees)');
        legend({'Gyro Data','Floor Data'});
        grid on;

        subplot(3,1,3), hYaw = plot(times,UnbiasAtt(:,3)*k); hold on;
        title('Estimated Yaw');
        xlabel('Time (seconds)');
        ylabel('Angle (degrees)');
        grid on;
        
        figure(3); clf(); hold on;
        h2dLaser = plot(0,0,'b.');
        hPoles = plot(0,0,'r*','markersize',10);
        laserTitleHandle = title('Simulated Laser Scan');
        xlabel('X-Axis (cm)');
        ylabel('Y-Axis (cm)');
        grid on;
        axis([0,200,-100,100]);
        
        uicontrol('Style','pushbutton','String','Pause/Cont.','Position',[10,1,80,20],'Callback',{@MyCallBackA,1});
        
        figure(4); clf(); hold on;
        initD = 40; initH = 89*pi/180;
        vehicle.x = 25; vehicle.y = 105; vehicle.h = initH;
        vehicleHist(:,1) = [vehicle.x; vehicle.y; vehicle.h];
        hVehicle = quiver(0,0,initD*cos(initH),initD*sin(initH),'r');
        hGPoles = plot(0,0,'r+','markersize',8);
        grid on;
        plot(Landmarks.xy(1,:),Landmarks.xy(2,:),'k*');
        hPath = plot(vehicleHist(1,:),vehicleHist(2,:),'k-');
        ax=axis();
        ax=ax+[-10,10,-10,10] ; axis(ax);
        xlabel('X (cm)');
        ylabel('Y (cm)');
        %title('Landmarks and initial pose of robot');
        
        figure(5);
        error1Hist(1) = 0;
        error2Hist(1) = 0;
        hError1 = plot(error1Hist,1,'r-');
        hold on;
        hError2 = plot(error2Hist,1,'b-');
        grid on;
        ylabel('Error');
        xlabel('Count');
        legend({'fminsearch','fmin+adjust'});
        

    end

    pause(0.1);  
    
    while (ABCD.flagPause), pause(0.2) ; continue ; end
    
end
          
    disp('Done....');

function newAtt = EulerIntegrate(Wx, Wy, Wz, dt, curAtt)
  
    curRoll = curAtt(1);
    curPitch = curAtt(2);
    curYaw = curAtt(3);

    newRoll = curRoll + dt * (Wx + (Wy * sin(curAtt(1)) + Wz * cos(curAtt(1))) * tan(curAtt(2)));
    newPitch = curPitch + dt * (Wy*cos(curAtt(1)) - Wz*sin(curAtt(1)));
    newYaw = curYaw + dt * ((Wy*sin(curAtt(1)) + Wz*cos(curAtt(1)))/cos(curAtt(2)));
    
    newAtt = [newRoll, newPitch, newYaw];
    
return;

end

function gPC = GlobalTransform(PC,veh)
    angle = veh.h - pi/2;
    trMat = [cos(angle) -sin(angle); sin(angle) cos(angle)];
    gPC = trMat*[-PC(2,:); PC(1,:)] + [veh.x; veh.y];
end

function rays = RayTransform(ray,veh,vang)
    angle = veh.h + vang;
    j = 1;
    for i = 1:length(vang)
        
        anglei = angle(i);
        trMat = [cos(anglei) -sin(anglei); sin(anglei) cos(anglei)];
        rays(j:j+1,:) = trMat*ray + [veh.x; veh.y];
        j = j + 2;
    end
end

function error = Adjustment(AOOI, PC, LMs, veh, xshift, yshift, angle)
    error = 0;
%     gPC = gPC - [veh.x; veh.y]; % bring back to origin
%     trMat = [cos(angle) -sin(angle); sin(angle) cos(angle)];
%     gPC = trMat*gPC + [veh.x + xshift; veh.y + yshift]; % set back to original position
    angle = veh.h - pi/2 + angle;
    trMat = [cos(angle) -sin(angle); sin(angle) cos(angle)];
    gPC = trMat*[-PC(2,:); PC(1,:)] + [veh.x + xshift; veh.y + yshift];
    
    for ii = 1:length(AOOI.globalIndex)
        if AOOI.globalIndex(ii) ~= 0
            gI = AOOI.globalIndex(ii);
            lI = AOOI.localIndex(ii);
            dx = LMs.xy(1,gI) - gPC(1,lI);
            dy = LMs.xy(2,gI) - gPC(2,lI);
            error = error + hypot(dx,dy);
        end
    end

end

function [xshift, yshift] = SecondAdjust(AOOI, PC, LMs, veh, xshift, yshift, angle)

    angle = veh.h - pi/2 + angle;
    trMat = [cos(angle) -sin(angle); sin(angle) cos(angle)];
    gPC = trMat*[-PC(2,:); PC(1,:)] + [veh.x + xshift; veh.y + yshift];
    
    dx = realmax*ones(1,length(AOOI.globalIndex));
    dy = realmax*ones(1,length(AOOI.globalIndex));
    for ii = 1:length(AOOI.globalIndex)

        if AOOI.globalIndex(ii) ~= 0
            gI = AOOI.globalIndex(ii);
            lI = AOOI.localIndex(ii);
            dx(ii) = LMs.xy(1,gI) - gPC(1,lI);
            dy(ii) = LMs.xy(2,gI) - gPC(2,lI);
        end
    end
    
    xshift = 0;
    yshift = 0;
    if min(dx) > 0  % all dx are same sign, so shift
        xshift = min(dx);
    elseif max(dx) < 0 % all dx are same sign, so shift
        xshift = max(dx);
    end
    if min(dy) > 0 
        yshift = min(dy);
    elseif max(dy) < 0
        yshift = max(dy);
    end
        
end

function AOOI = FindAOOI(gPC, LMs)
    %disp(gPC);
    %disp(LMs);
    AOOI.globalIndex = [];
    AOOI.localIndex = [];

    for i = 1:numel(gPC)/2
        for j = 1:length(LMs.xy)
            dx = gPC(1,i) - LMs.xy(1,j);
            dy = gPC(2,i) - LMs.xy(2,j);
            dist = hypot(dx,dy);
           
            if dist < 15
                AOOI.globalIndex(i) = j;
                break;
            else
                AOOI.globalIndex(i) = 0;
            end          
        end
    end
    
    %AOOI.globalIndex = AOOI.globalIndex(AOOI.globalIndex > 0);
    %AOOI.localIndex = AOOI.localIndex(AOOI.localIndex > 0);
    
    AOOI.globalIndex = unique(AOOI.globalIndex,'stable');
       
    for i = 1:length(unique(AOOI.globalIndex))
        if AOOI.globalIndex(i) ~= 0
            minDist = realmax;
            
            for j = 1:numel(gPC)/2
                dx = LMs.xy(1,AOOI.globalIndex(i)) - gPC(1,j);
                dy = LMs.xy(2,AOOI.globalIndex(i)) - gPC(2,j);
                dist = hypot(dx,dy);

                if dist < minDist
                    minDist = dist;
                    AOOI.localIndex(i) = j;
                end

            end
        end
    end
    disp(AOOI);
end

function DefineOG( )
    global MyContext
    % Define a structure, with parameters useful for processing an Occupancy
    % Grid.
    % here I define a Matrix for storing the values of the OG (I call it "M")
    MyContext.M = zeros(MyContext.Ny,MyContext.Nx,'double') ;
    % These constants are useful for scaling (x,y) points to cells' indexes.

    MyContext.Cx = MyContext.Nx/(MyContext.x2-MyContext.x1) ;
    MyContext.Cy = MyContext.Ny/(MyContext.y2-MyContext.y1) ;
    return;
end
% ...........................................................
% This function transforms points (x,y), for obtaining their equivalent cells' indexes.
% and then use them to set the related cells
function PopulatePC(gPC, rayIndex)
    global MyContext
    x = gPC(1,:);
    y = gPC(2,:);
    
    % Firstly, we filter out those points that are outside our ROI (Region of Interest).
    % We keep the valid ones.
    ii = find((x>=MyContext.x1)&(x<MyContext.x2)&(y>=MyContext.y1)&(y<MyContext.y2));
    x=x(ii) ; y=y(ii) ;
    % just consider the points that are inside the OG's coverage
    % convert to indexes
    ix = floor((x-MyContext.x1)*MyContext.Cx)+1 ;
    iy = floor((y-MyContext.y1)*MyContext.Cy)+1 ;
    % Convert 2D indexes to linear indexes
    ixy = sub2ind(size(MyContext.M),iy,ix) ;
    % Set the associated OG's cells, to contain value =1
    ixy = intersect(ixy,rayIndex);
    prevPos = find(MyContext.M <= 0.5 & MyContext.M ~= 0);
    prevPos = intersect(prevPos,rayIndex);
    MyContext.M(prevPos) = MyContext.M(prevPos) - 0.05;
    MyContext.M(MyContext.M < 0) = 0;
    MyContext.M(ixy) = 0.5;
    
%     prevPos = find(MyContext.M <= 0.7 & MyContext.M ~= 0);
%     MyContext.M(prevPos) = MyContext.M(prevPos) - 0.05;
%     negVal = find(MyContext.M < 0);
%     MyContext.M(negVal) = 0;
%     MyContext.M(ixy) = 0.7;
end

function PopulateVeh(veh)
    global MyContext
    x = veh.x;
    y = veh.y;
    
    % Firstly, we filter out those points that are outside our ROI (Region of Interest).
    % We keep the valid ones.
    ii = find((x>=MyContext.x1)&(x<MyContext.x2)&(y>=MyContext.y1)&(y<MyContext.y2));
    x=x(ii) ; y=y(ii) ;
    % just consider the points that are inside the OG's coverage
    % convert to indexes
    ix = floor((x-MyContext.x1)*MyContext.Cx)+1 ;
    iy = floor((y-MyContext.y1)*MyContext.Cy)+1 ;
    % Convert 2D indexes to linear indexes
    ixy = sub2ind(size(MyContext.M),iy,ix) ;
    % Set the associated OG's cells, to contain value =1
    
    prevPos = find(MyContext.M == 0.95);
    prevPos = prevPos(prevPos ~= ixy);
    if ~isempty(prevPos)
        MyContext.M(prevPos) = 0.9;
    end
    MyContext.M(ixy) = 0.95;
end

function rayIndex = PopulateRay(rays)
    global MyContext
    
    x = [];
    y = [];
    
    for i = 1:2:119
        x = [x rays(i,:)];
        y = [y rays(i+1,:)];
    end
    
    % Firstly, we filter out those points that are outside our ROI (Region of Interest).
    % We keep the valid ones.
     ii = find((x>=MyContext.x1)&(x<MyContext.x2)&(y>=MyContext.y1)&(y<MyContext.y2));
    x=x(ii) ; y=y(ii) ;
    % just consider the points that are inside the OG's coverage
    % convert to indexes
    ix = floor((x-MyContext.x1)*MyContext.Cx)+1 ;
    iy = floor((y-MyContext.y1)*MyContext.Cy)+1 ;
    % Convert 2D indexes to linear indexes
    ixy = sub2ind(size(MyContext.M),iy,ix) ;
    % Set the associated OG's cells, to contain value =1
    prevPos = find(MyContext.M == 0.8);
    MyContext.M(prevPos) = 0;
    %nonBlk = find(MyContext.M ~= 0);
    %ixy = setdiff(ixy,nonBlk);
    poleLocation = find(MyContext.M <= 0.75 & MyContext.M ~= 0);
    ixy = setdiff(ixy,poleLocation);
    MyContext.M(ixy) = 0.8;
    rayIndex = ixy;
end



function MyCallBackA(~,~,x)  % function for buttons
    global ABCD;
        
    if (x==1)
       ABCD.flagPause = ~ABCD.flagPause; %Switch ON->OFF->ON -> and so on.
       return;
    end
end

