function []=fuzzyVirtual()
% sim car in random movement

clc; clear all; close all; dbstop if error;
set(0,'defaultaxesfontname','times new roman');

field.range=80;
time.dt=1; time.T=300;

[fig]=FigureNew(field);
[car]=CarNew();
[car]=CarNow(car, time, 0,0);
[car]=CarShow(car,0);
[target]=TargetNew();
distance = 65;

fis_vel = readfis('MTRN4010_vel.fis');
fis_ang = readfis('MTRN4010_ang1.fis');

PS.XLB=5;
PS.XUB=25;
PS.D=1;
PS.G=20;
PS.N=10;
PS.V=rand(PS.D,PS.N);
PS.Gbest = [];
PS.gbest = realmax;
PS.Pbest = [];
PS.pbest = ones(1, PS.N)*realmax;
PS.w1=0.9;
PS.w2=0.4;
PS.dw=PS.w1-PS.w2;
PS.cg=2;
PS.cp=2;
PS.X=rand(PS.D,PS.N);
PS.BND=[PS.XLB PS.XUB];
flag = 0;
PS.X(1,:)=PS.BND(1,1)+PS.X(1,:)*diff(PS.BND(1,:));

carinit=car;
for g=1:PS.G
    disp(PS.X);
    car=carinit;
    for n=1:PS.N 
        distance=PS.X(1,n);
        virtualTarget1 = virtualNew(target.x, target.y, target.q, distance);
        virtualTarget1.z = 0;
        car=carinit;
        flag=0;
        for t=0:time.dt:time.T,
          if(virtualTarget1.z ~= 1)
              [virtualTarget1]=virtualUpdate(time, virtualTarget1, target);
          end
          virtualTarget = TargetShow(virtualTarget1);
          [ds]=FindDistance(car,virtualTarget1);
          vel=evalfis(ds,fis_vel);
          [dq] = FindAngular(car,virtualTarget1);
          ang=evalfis(dq,fis_ang);
          [car]=CarNow(car,time,vel,ang);
          [car]=CarShow(car,t);
        end;
        err=[car.x-target.x car.y-target.y wrapToPi(car.q-target.q)*pi/180] %maybe change to radians
        fit(n)=sqrt(sum(err.^2));
        if fit(n)<PS.gbest,
             PS.gbest=fit(n);
             PS.Gbest=PS.X(:,n);
        end
        if fit(n)<PS.pbest(n),
             PS.pbest(n)-fit(n);
             PS.Gbest=PS.X(:,n);
        end
        if fit(n)<PS.pbest(n),
            PS.pbest(n)=fit(n);
            PS.Pbest(:,n)=PS.X(:,n);
        end;
        reset(virtualTarget1.hdL.shape);
    end;
    %xlabel('Generation %d - Particle %d', g, n);
    disp(sprintf('Generation %d Gbest %5.3f %5.3f gbest %5.3f', g, PS.Gbest.',PS.gbest));
    w = PS.w2+(1-g/PS.G)*PS.dw;
    PS.V=w*rand(PS.D,PS.N).*PS.V+PS.cp*rand(PS.D,PS.N).*(PS.Pbest-PS.X)+...
    PS.cg*rand(PS.D,PS.N).*(repmat(PS.Gbest,[1,PS.N])-PS.X);
    PS.X=PS.X+PS.V;
    for d=1:PS.D,
        z=find(PS.X(d,:)<PS.BND(d,1));
        PS.X(d,z)=PS.BND(d,1)+rand(1,length(z))*diff(PS.BND(d,:));
        z=find(PS.X(d,:)>PS.BND(d,2));
        PS.X(d,z)=PS.BND(d,1)+rand(1,length(z))*diff(PS.BND(d,:));
    end
end
            

car=carInit;

for t=0:time.dt:time.T,
  if(virtualTarget.z ~= 1)
      [virtualTarget]=virtualUpdate(time, virtualTarget, target);
  end
  TargetShow(virtualTarget);
  [ds]=FindDistance(car,virtualTarget);
  vel=evalfis(ds,fis_vel);
  [dq] = FindAngular(car,virtualTarget);
  ang=evalfis(dq,fis_ang);
  [car]=CarNow(car,time,vel,ang);
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
rangeMin = -50;
rangeMax = 50;
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

function [virTarget]=virtualNew(x, y, bearing, distance)
    virTarget.q=bearing;
    virTarget.x = (x)-distance*cos(virTarget.q);
    virTarget.y = (y)-distance*sin(virTarget.q);
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
        virtualTarget.x=virtualTarget.x+time.dt*0.075*cos(virtualTarget.q);
        virtualTarget.y=virtualTarget.y+time.dt*0.075*sin(virtualTarget.q);    
    end
    
function [target]=TargetShow(target)
    Rz=[  cos(target.q) -sin(target.q); 
       sin(target.q)  cos(target.q)];
    shape=Rz*target.shape+repmat([target.x;target.y],1,6);
    set(target.hdL.shape,'xdata',shape(1,:),'ydata',shape(2,:)); 
    set(target.hdL.trace,'xdata',target.trace(1,:),'ydata',target.trace(2,:));

    