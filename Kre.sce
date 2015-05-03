// Ã©quations diffÃ©rentielles rÃ©gissant l'alunissage de Lunar Lander
Te=0.04            // pÃ©riode d'Ã©chantillonnage (s)
mvide= 6000;  // masse Ã  vide (kg)
mfuel=800;    // masse de carburant (kg)
m = mvide + mfuel; // masse totale de Lunar Lander (en kg)
ve=4500;                   // vitesse d'Ã©jection des gaz ou specific impulse (en m/s), 
erg = ve/m;                // coefficient de poussÃ©e des rÃ©acteurs (en m/(s*kg)),
maxThrust = 25; // dÃ©bit de carburant maximum dans les rÃ©acteurs (en kg/s) 
g= 9.807;                  // accÃ©lÃ©ration de la pesanteur lunaire (en m/s^2)
X0=[534, 0, 489, 0]'; // vecteur d'Ã©tat initial X=[x, vx, y, vy]
A= [0, 1, 0, 0
       0, 0, 0, 0
       0, 0, 0, 1
       0, 0, 0, 0];   // matrice d'Ã©tat (4x4)
B=[0,     0
      erg, 0
      0,     0
      0,  erg];   // matrice de commande (2x4)
C=eye(4,4);  // matrice d'observation, 4x4
D=zeros(4,2);
lem=syslin('c',A,B,C,D); // lem contient la reprÃ©sentation d'Ã©tat de Lunar Lander
//  NOTE: il faudra retrancher g/erg Ã  la commande ay du rÃ©acteur vertical
// 
// discretisation du processus d'alunissage
lemd=dscr(lem,Te);
ad=lemd('a');
bd=lemd('b');

// calcul d'une loi de commande par retour d'Ã©tat par 
// pÃ´les complexes conjuguÃ©s dÃ©sirÃ©s :
m=sqrt(2)/2;     //amortissement rÃ©duit, pour tr5 minimum

tr5 = 4;         //temps de rÃ©ponse Ã  5%, en secondes
omega0=2.93/tr5; // pulsation propre (rd/s)
// vecteur contenant les valeurs propres dÃ©sirÃ©es
vp=omega0*[-m+%i*sqrt(1-m**2),-m-%i*sqrt(1-m**2)];
vp=[vp,vp];
// discrÃ©tisation des valeurs propres du systÃ¨me bouclÃ© :
vz=exp(Te*vp);

// Noter que si on souhaitait des valeurs propres rÃ©elles
// on pourrait faire par exemple
// vpr=[-3/tr,-3/tr,-30/tr,-30/tr];

// calcul de la matrice de gain du retour d'Ã©tat
// avec Scilab on doit sÃ©parer le calcul de Kh sur l'axe x et Ky sur l'axe y
Kh=ppol(ad(1:2,1:2),bd(1:2,1),vz(1:2)) // retour d'Ã©tat horizontal
Kv=ppol(ad(3:4,3:4),bd(3:4,2),vz(3:4)) // retour d'Ã©tat vertical
K=[Kh,0,0;0,0,Kv]

// vÃ©rification
spec(ad-bd*K)

// calcul de la trajectoire
com=[];
X=X0;
traj=X;
t=0;
temps=[t];
while X(3)>0.05*X0(3) // pour les rÃ©gimes apÃ©riodiques
   dcom= -K*X;
   com= [com; dcom];
   X= ad*X+bd*dcom;
   traj= [traj, X];
   t= t+Te;
   temps= [temps, t]; 
 end
plot2d(traj(1,:),traj(3,:))
xgrid();

// performances de la commande
ax=com(1:2:length(com));  // commande horizontale (selon x)
ay=com(2:2:length(com))+g/erg; // commande verticale (selon y)
maxax=string(round(max(abs(ax)))) // commande maximum
maxay=string(round(max(abs(ay)))) // idem
carburant=fix(sum(abs(ax))+sum(abs(ay)))*Te 
carburant=string(carburant); // carburant cumulÃ©
figure(2)
plot(temps,traj)
legend('x','vx','y','vy')
title(['Carburant utilisÃ©: ',carburant, ...
       ', commande max x: ',maxax,' max y: ',maxay])
xlabel(['durÃ©e alunissage: ', string(t),' secondes.'])
ylabel('alunissage lunar module : commande en vitesse')
xgrid

//Ã©crire dans le fichier Kre.txt
    //chemin='D:\DISQUED\2.Cours\3.IntCom\LunarLanderControl\';
    fileid='C:\Users\Hugo\Desktop\Kre.txt';
    fp=mopen(fileid, 'w');
    Kt=K';
    Ks=string(K(1));
    for k=2:length(K),
        Ks=Ks+','+string(Kt(k));
    end
    mputstr(Ks,fp);
    mclose(fp);
