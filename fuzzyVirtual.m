function []=fuzzyVirtual()
% sim car in random movement

clc; clear all; close all; dbstop if error;
set(0,'defaultaxesfontname','times new roman');

field.range=80;
time.dt=1; time.T=500;

[fig]=FigureNew(field);
[car]=CarNew();
[car]=CarNow(car, time, 0,0)
[car]=CarShow(car,0);
[target]=TargetNew();
[virtualTarget]=virtualNew(target.x, target.y, target.q);
virtualTarget=TargetShow(virtualTarget);
virtualTarget.z = 0;
fis_vel = readfis('MTRN4010_vel.fis');
fis_ang = readfis('MTRN4010_ang.fis');

for t=0:time.dt:time.T,
  if(virtualTarget.z ~= 1)
      [virtualTarget]=virtualUpdate(time, virtualTarget, target);
  end
  TargetShow(virtualTarget);
  [ds]=FindDistance(car,virtualTarget);
  vel=evalfis(ds,fis_vel);
  [dq] = FindAngular(car,virtualTarget);
  ang=evalfis(dq,fis_ang);
  [car]=CarNow(car,time,vel,ang)
  [car]=CarShow(car,t);
end;

function [ds]=FindDistance(car,target)
dx = target.x - car.x;
dy = target.y - car.y;
ds = sqrt(dx.*dx + dy.*dy);

function [dq]=FindAngular(car,target)
dx = target.x - car.x;
dy = target.y - car.y;
dq = mod((atan2(dy,dx) - car.q) + pi, 2*pi) - pi;

function [fig]=FigureNew(field)
fig=figure('units','normalized','position',[0.1 0.2 0.5 0.5]);
axis([-1 1 -1 1]*field.range); hold on; grid on; axis equal;
xlabel('x-direction'); ylabel('y-direction');

function [car]=CarNew()
rangeMin = -65;
rangeMax = 65;
rangeTot = (rangeMax - rangeMin).*rand(2,1) + rangeMin;
car.x=rangeTot(1); car.y=rangeTot(2); car.q= 360 * rand(1, 1);
car.trace=[car.x; car.y; car.q];
car.shape=[ 2 0; 1 1; -1 1; -1 -1; 1 -1; 2 0]';
car.hdL.shape=plot(car.shape(1,:),car.shape(2,:),'color','b','linewidth',1);
car.hdL.trace=plot(car.trace(1,:),car.trace(2,:),'color',[0 0.66 0]);

function [target]=TargetNew();
rangeMin = -25;
rangeMax = 25;
rangeTot = (rangeMax - rangeMin).*rand(2,1) + rangeMin;
target.x=rangeTot(1) ;
target.y=rangeTot(2) ;
target.q=360 * rand(1, 1);
Rz=[  cos(target.q) -sin(target.q); 
     sin(target.q)  cos(target.q)];
target.trace=[target.x; target.y; target.q];
target.shape=[ 2 0; 1 1; -1 1; -1 -1; 1 -1; 2 0]';
shape=Rz*target.shape+repmat([target.x;target.y],1,6);
target.hdL.shape=plot(shape(1,:),shape(2,:),'color','r','linewidth',1);
target.hdL.trace=plot(target.trace(1,:),target.trace(2,:),'color',[0 0.66 0]);
ax=axis;
axis(ax); title(sprintf('Time %d',0)); pause(0.001);

function [car]=CarNow(car,time,v,w)
car.x=car.x+time.dt*v*cos(car.q);
car.y=car.y+time.dt*v*sin(car.q);
car.q=car.q+time.dt*w;
car.trace(:,end+1)=[car.x; car.y; car.q];

function [car]=CarShow(car,t)
ax=axis;
Rz=[  cos(car.q) -sin(car.q); 
      sin(car.q)  cos(car.q)];
shape=Rz*car.shape+repmat([car.x;car.y],1,6);
set(car.hdL.shape,'xdata',shape(1,:),'ydata',shape(2,:)); 
set(car.hdL.trace,'xdata',car.trace(1,:),'ydata',car.trace(2,:));
axis(ax); title(sprintf('Time %d',t)); pause(0.001);

function [virTarget]=virtualNew(x, y, bearing)
    virTarget.q=bearing;
    virTarget.x = (x)-30*cos(virTarget.q);
    virTarget.y = (y)-30*sin(virTarget.q);
    virTarget.shape=[ 2 0; 1 1; -1 1; -1 -1; 1 -1; 2 0]';
    virTarget.trace=[virTarget.x; virTarget.y; virTarget.q];
    shape=virTarget.shape+repmat([virTarget.x;virTarget.y],1,6);
    virTarget.hdL.shape=plot(shape(1,:),shape(2,:),'color','m','linewidth',1);
    virTarget.hdL.trace=plot(virTarget.trace(1,:),virTarget.trace(2,:),'color',[0 0.66 0]);
    
function [virtualTarget]=virtualUpdate(time,virtualTarget, target)
    %{
    if (((virtualTarget.x)/(target.x)>0.95 && ((virtualTarget.x)/(target.x))<1) || ((virtualTarget.x)/(target.x)<-0.95 && ((virtualTarget.x)/(target.x))>-1))
        virtualTarget.x=target.x;
        virtualTarget.y=target.y;
        virtualTarget.x;
    %}
    if (abs(virtualTarget.x - target.x)<0.1)
        virtualTarget.x=target.x;
        virtualTarget.y=target.y;
        virtualTarget.z=1;
        return
    else  
        virtualTarget.x=virtualTarget.x+time.dt*0.15*cos(virtualTarget.q);
        virtualTarget.y=virtualTarget.y+time.dt*0.15*sin(virtualTarget.q);    
    end
    
function [target]=TargetShow(target)
    Rz=[  cos(target.q) -sin(target.q); 
       sin(target.q)  cos(target.q)];
    shape=Rz*target.shape+repmat([target.x;target.y],1,6);
    set(target.hdL.shape,'xdata',shape(1,:),'ydata',shape(2,:)); 
    set(target.hdL.trace,'xdata',target.trace(1,:),'ydata',target.trace(2,:));

    