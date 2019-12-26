package back_end;

import java.util.ArrayList;

public class Link{
    //ATTRIBUTS
    int poids;
    String userFinal;
    String userInital;

    //CONSTRUCTEUR
    public Link(int poids, String userFinal, String userInital){ //ordre alphabétique F<I : user final then initial
        this.poids = poids;
        this.userFinal = userFinal;
        this.userInital = userInital;
    }

    //GETTERS AND SETTERS
    public int getPoids(){
        return poids;
    }

    public String getUserInital(){
        return userInital;
    }

    public String getUserFinal() {
        return userFinal;
    }

    public void setPoids(int poids) {
        this.poids = poids;
    }

    public void setUserFinal(String userFinal) {
        this.userFinal = userFinal;
    }

    public void setUserInital(String userInital) {
        this.userInital = userInital;
    }

}
