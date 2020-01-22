


public class Vecteur {
    private double x;
    private double y;
    public Vecteur(double x, double y){
        this.x = x;
        this.y = y;
    }
    public void move (double c, double e){
        this.x = this.x + c;
        this.y = this.y + e;
    }
    public void move(double c){
        this.x = this.x + c;
        this.y = this.y + c;
    }
    public Vecteur(){
        this(0.0,0.0);
    }
    public Vecteur(double d){
        this(Math.random()*d -d,Math.random()*d - d);
    }
    public void soustraction(Vecteur f){
        this.x = this.x - f.x;
        this.y = this.y - f.y;
    }
    public Vecteur clone(){
        return new Vecteur(this.x, this.y);
    }
    public double scalaire (Vecteur d){
        return this.x*d.x + this.y*d.y;
    }
    public double norme(Vecteur d){
        return Math.sqrt(d.x*d.x + d.x*d.y);
    }
    public boolean egalite(Vecteur d){
        return (this.x == d.x ) && (this.y == d.y);
    }
    public double angle(Vecteur d){
        double fres = scalaire(d)/norme(d)*norme(this);
        return Math.acos(fres);
    }
}
