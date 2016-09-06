

	public class XYZPoint {
		  double x;
		  double y;
		  double z;

	    public XYZPoint(double x, double y, double z) {
	        this.x = x;
	        this.y = y;
	        this.z=z;
	    }


	    public double getX() {
	        return x;
	    }

	    public double getY() {
	        return y;
	    }
	    public double getZ(){
	    	return z;
	    }
	    public String toString() {
	        return ("(" + x + "," + y + "," + z + ")"); 
	    }
	}




