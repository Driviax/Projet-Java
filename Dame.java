public class Dame {
    public static char[][] baseDame() {
        //Tableau à 2 dimention qui forme la base
        char [][] base = new char[10][10];
        //si la somme de i+j et pair alors la valeur char='*' sinon ' '
        for (int i=0 ; i<10 ; i++) {
            for (int j=0 ; j<10 ; j++) {
                if ((j+i)%2==0) {
                    base[i][j]='*';
                } else {
                    base[i][j]=' ';
                }
            }
        }
        return base; //base de case blanche=' /noir='*'
    }

    public static char[][] dameBlanche() {
        char [][] blanc = baseDame();
        //utilise la fonction baseDame, ajoute les pions blanc 'b'
        for (int i=0 ; i<4 ; i++) {
            for (int j=0 ; j<10 ; j++) {
                if ((j+i)%2==0) {
                    blanc[i][j]='b';
                } else {
                    blanc[i][j]=' ';
                }
            }
        }

        return blanc;
    }

    public static char[][] dameNoir() {
        char [][] noir = dameBlanche();
        //baseDame+dameBlacnhe et ajoute les dameNoir
        for (int i=0 ; i<4 ; i++) {
            for (int j=0 ; j<10 ; j++) {
                if ((j+i)%2==0) {
                    noir[i+6][j]='n';
                } else {
                    noir[i+6][j]=' ';
                }
            }
        }

        return noir;
    }

//----------------------------------------------------------------------------------------------
//systeme de déplacement
    //systeme de selection du départ
    public static int[] selection(String couleur, char [][] table) {
        int x,y,sens;
        char pion;

        //selection de la couleur et du sens
        if (couleur.equals("noir")) {
            pion = 'n';
            sens = -1;
        } else {
            pion = 'b';
            sens = 1;
        }

        //selection et verification des coordonnées
        Ecran.afficherln("Equipe ",couleur,". Quelle pièce souhaites-tu déplacer ? ");
        Ecran.afficherln("Coordonnée y : ");
        y = Clavier.saisirInt();
        Ecran.afficherln("Coordonnée x : ");
        x = Clavier.saisirInt();

        while (x<1||x>10||y<1||y>10||table[y-1][x-1]!=pion){
                Ecran.afficherln("Coordonnées erronées !");
                Ecran.afficherln("Coordonnée y : ");
                y = Clavier.saisirInt();
                Ecran.afficherln("Coordonnée x : ");
                x = Clavier.saisirInt();
            }    
        Ecran.afficherln("Dame ",couleur," bien séléctionnée !");

        return new int[]{y-1,x-1,sens};
    }

    //systeme de selection de l'arrive
    public static int[] deplacementVers(char[][] table) { 
        int x,y;

        //selection de n'importe qu'elle case sur la table (si existe !)
        Ecran.afficherln("Ou souhaites-tu aller ?");
        Ecran.afficherln("Coordonnée y : ");
        y = Clavier.saisirInt();
        Ecran.afficherln("Coordonnée x : ");
        x = Clavier.saisirInt();
        
        while (x<1||x>10||y<1||y>10||table[y-1][x-1]!='*'){
                Ecran.afficherln("Coordonnées erronées !");
                Ecran.afficherln("Coordonnée y : ");
                y = Clavier.saisirInt();
                Ecran.afficherln("Coordonnée x : ");
                x = Clavier.saisirInt();
        }
        return new int[]{y-1,x-1};
    }

    public static char[][] deplacer(char[][] table, int [] depart, int [] arrive) {
        int ligne,colonne,sens;
        boolean valide;
        char pion;

        //si ligne=sens alors le pien ce déplace bien vers l'avant selon la couleur noir=-1, blanc=1
        ligne = arrive[0]-depart[0];
        sens = depart[2];

        //la colonne ne peut que varier entre le nombre 1 et -1 (gauche ou droite)
        colonne = arrive[1]-depart[1];
        
        //determine la couleur grace au sens reçu
        if (sens==-1) {
            pion='n';
        } else {
            pion='b';
        }

        //le joueur peut essayer d'attaquer quant il le souhaite, mais si la fonction attaque retourne une réponse négative alors le joueur est contraint de ce déplacer normalement
        valide = sens==ligne && (colonne==1 || colonne==-1);

        //verifie si le joueur attaque ou se déplace sinon erreur
        while (valide==false&&attaque(arrive, depart, table)==false) {
            Ecran.afficherln("Valeur d'arrivée impossible...");
            arrive = deplacementVers(table);
            ligne = arrive[0]-depart[0];
            colonne = arrive[1]-depart[1];
            valide = sens==ligne && colonne==1 || colonne==-1;
        }

        //attaque ou se déplace
        if (attaque(arrive, depart, table)==true) {
            table[(depart[0]+arrive[0])/2][(depart[1]+arrive[1])/2]='*';
            Ecran.afficherln("Déplacement accepté");
            table[arrive[0]][arrive[1]]=pion;
            table[depart[0]][depart[1]]='*';
        } else {
            Ecran.afficherln("Déplacement accepté");
            table[arrive[0]][arrive[1]]=pion;
            table[depart[0]][depart[1]]='*';
        }
        

        //Effectue l'echange de place et de valeurs

        return table;
    }

    public static boolean attaque(int[] arrive, int[] depart, char[][] table) {
        int[] millieu=new int[2];
        char pion;
        boolean centre, diff;
        
        if (depart[2]==-1) {
            pion='b';
        } else {
            pion='n';
        }

        millieu[0]=(arrive[0]+depart[0])/2;
        millieu[1]=(arrive[1]+depart[1])/2;

        diff = arrive[1]==depart[1]+2 || arrive[1]==depart[1]-2 && arrive[0]==depart[0]+2 || arrive[0]==depart[0]-2;
        centre = pion==table[millieu[0]][millieu[1]]&&table[millieu[0]][millieu[1]]!=table[depart[0]][depart[1]];

        //Regarde la case au milieu de la valeur de départ et d'arrivé
        if (centre&&diff) {
            return true;
        } else return false;
    }

    public static boolean rejouer() {
        String reponse;

        Ecran.afficherln("Tu viens d'attaquer, veux continuer ? (y/n)");
        reponse = Clavier.saisirString();

        while (reponse!="y"||reponse!="n") {
            Ecran.afficherln("Réponse invalide : (1 ou 0)");
            reponse = Clavier.saisirString();
        }

        if (reponse=="y") {
            return true;
        } else return false;
        
        
    }

//----------------------------------------------------------------------------------------------
//Score
//Entrée char[][] grille de jeu
public static int[] score(char[][] table) {
    //Sortie comte le nombre de pion de chaque équipe
    int[] points=new int[2];
    
    points[0]=0;
    points[1]=0;

    for (int i=0; i<10 ; i++) {
        for (int j=0; j<10 ; j++) {
            if (table[i][j]=='n') {
                points[0]=points[0]+1;
            }
            if (table[i][j]=='b') {
                points[1]=points[1]+1;
            }
        }
    }
    
    return points;
}


//----------------------------------------------------------------------------------------------

//Affichage
public static void affichage(char[][] table) {
    Ecran.afficherln("     1   2   3   4   5   6   7   8   9   10 ");
        for (int i=0 ; i<10 ; i++) {
            Ecran.afficherln("   +---+---+---+---+---+---+---+---+---+---+");
            if (i+1==10) {
                Ecran.afficher(i+1," | ");    
            } else {
                Ecran.afficher(i+1,"  | ");
            }
            for (int j=0 ; j<10 ; j++) {
                Ecran.afficher(table[i][j]," | ");
            }
            Ecran.afficherln(" ",i+1);
        }
        Ecran.afficherln("   +---+---+---+---+---+---+---+---+---+---+");
        Ecran.afficherln("     1   2   3   4   5   6   7   8   9   10 ");
}


//----------------------------------------------------------------------------------------------

    public static void main (String []args){
        //affiche le tableau crée plutôt
        char [][] dame = dameNoir();
        int[] points= score(dame);
        affichage(dame);
        int totaux1, totaux2;
        
        while (points[0]!=0&&points[1]!=0) {
            points=score(dame);
            totaux2=points[0]+points[1];
            deplacer(dame,selection("blanche", dame), deplacementVers(dame));
            affichage(dame);
            points=score(dame);
            totaux1=points[0]+points[1];
            deplacer(dame,selection("noir", dame), deplacementVers(dame));
            affichage(dame);
        }

        if (points[0]==0) {
            Ecran.afficherln("L'équipe blanche a gagné !");
        } else Ecran.afficherln("L'équipe noir a gagné !");

        Ecran.afficher("Merci d'avoir joué !!!");
    }
}