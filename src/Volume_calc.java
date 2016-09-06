
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class Volume_calc {
	 static int newInt=1;
	//36 conditions
	//89 subjects
static boolean probs;
	//take volume of 4 points, starting top left and going clockwise
	//returns the volume between four xyz points
	public static double takeVolume( XYZPoint a,XYZPoint b, XYZPoint c, XYZPoint d ){
		double area= (findAvgX(a,b,c,d))*(findAvgY(a,b,c,d));
		double avgHeight=(findAvgZ(a,b,c,d));
		
		return (area*avgHeight);
		
	}
	public static double findAvgX(XYZPoint a,XYZPoint b, XYZPoint c, XYZPoint d ){
		return Math.abs((a.getX())-Math.abs(c.getX()));
	}
	public static double findAvgY(XYZPoint a,XYZPoint b, XYZPoint c, XYZPoint d ){
		return Math.abs((a.getY())-Math.abs(c.getY()));
	}
	public static double findAvgZ(XYZPoint a,XYZPoint b, XYZPoint c, XYZPoint d){
		return (a.getZ()+b.getZ()+c.getZ()+d.getZ())/4;
	}


	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		@SuppressWarnings("resource")
		Scanner reader = new Scanner(System.in);
		System.out.println("Enter the number of X conditions");
		int numConditionsX = reader.nextInt();
		System.out.println("Enter the number of Y conditions");
		int numConditionsY = reader.nextInt();
		System.out.println("Enter the number of subjects");
		int numSubjects= reader.nextInt();
		System.out.println(" Enter the delayed probabilistic amount");
		double dpa= reader.nextDouble();
		System.out.println("should we read the data as probabilities, not odds? Reply Y for yes and N for no");
		String prob= reader.next();
		//choose whether to read as probability or odds
		if ( prob.equals("Y")|| prob.equals("y")){
			probs=true;
		}
		//input file name, whatever file data is stored in.
		File file = new File("/Users/graceportelance/Desktop/input data2(1).txt");
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(file);
		int numConditions= numConditionsX*numConditionsY;
		double[][] tempArray= new double[numConditions][numSubjects+2];
		XYZPoint [][] XYZpoints= new XYZPoint[numConditionsX][numConditionsY];
		double[] volumes= new double[numConditions];
		double[] finalValues=new double[numSubjects];
		int boxes= (numConditionsX-1)*(numConditionsX-1);
		double [][] subValues= new double[boxes][3];
		int i=0;
		int j=0;
		//Two writers to write the files and named before running-will write two txt files with output
		PrintWriter summary= new PrintWriter("lwh data.txt");
		@SuppressWarnings("resource")
		PrintWriter results= new PrintWriter("results data.txt");
		//read the data file into an array
		while (j<numSubjects+2){
			while (i<numConditions){
				double d=scanner.nextDouble(); 
				tempArray[i][j]=d;
				i++;
			}
			i=0;
			j++;
		}
		
		//Alter the first row if we want it in terms of probability
		if (probs==true){
			for (int q=0; q<numConditions; q++){
				double swap=(tempArray[q][0])/(1+tempArray[q][0]);
				tempArray[q][0]=swap;
			}
			
		}
		//determine the max x and y condition for each
		double maxX=0.0;
		double maxY=0.0;
		for (int p=0; p<numConditions; p++){
			if (tempArray[p][0]>maxX){
				maxX=tempArray[p][0];
			}
			if (tempArray[p][1]>maxY){
				maxY=tempArray[p][1];
			} 
		}
	
	//create a 'grid' with the first condition (x,y) in the upper lefthand corner
	//final condition (x,y) in the bottom righthand corner
	for (int k=2; k<numSubjects+2; k++){
		int p=0;
		for (int m=0; m<numConditionsY; m++){
			for (int n=0; n<numConditionsX; n++){
				if (probs==false){
				//use odds 
				XYZpoints[n][m]= new XYZPoint (tempArray[p][0]/maxX, tempArray[p][1]/maxY, tempArray[p][k]/dpa);
				p++;
				}
				else{
					//use probabilities
					XYZpoints[n][m]= new XYZPoint ((tempArray[p][0]*(1/maxX)), tempArray[p][1]/maxY, tempArray[p][k]/dpa);
					p++;
				}
			}
		}
		//create 'boxes' with the upper lefthand corner of each box as reference.
		//take the volume of each box
		int count=0;
		for ( int a=0; a<numConditionsX-1; a++){
			for (int b=0;b<numConditionsY-1; b++){
				double vol=takeVolume(XYZpoints[a][b],XYZpoints[a+1][b], XYZpoints[a+1][b+1], XYZpoints[a][b+1]);
				volumes[count]=vol;
				count++;
			}
		}
		//create the 5x5 array for data smoothing
		summary.println("Subject " + newInt + " subjective values");
		summary.println("Width" + "   " + "Length" + "   "+ "Height");
//		System.out.println("Subject " + newInt + " subjective values");
//		System.out.println("Width" + "   " + "Length" + "   "+ "Height");
		for ( int a=0; a<XYZpoints.length-1; a++){
			for (int b=0;b<XYZpoints.length-1; b++){
				subValues[a][0]=(Math.abs((XYZpoints[a][b].getX()-XYZpoints[a+1][b+1].getX()))/2)+XYZpoints[a][b].getX() ;
				subValues[a][1]=(Math.abs((XYZpoints[a][b].getY()-XYZpoints[a+1][b+1].getY()))/2)+ XYZpoints[a][b].getY() ;
				subValues[a][2]=(findAvgZ(XYZpoints[a][b],XYZpoints[a+1][b], XYZpoints[a+1][b+1], XYZpoints[a][b+1]));
				summary.println((double)Math.round(subValues[a][0]*10000)/10000 + "    " +
				((double)Math.round(subValues[a][1]*10000)/10000) + "      " + 
						((double)Math.round(subValues[a][2]*10000)/10000));
				//option to print to the console:
//				System.out.println((double)Math.round(subValues[a][0]*10000)/10000 + "    " +
//				 ((double)Math.round(subValues[a][1]*10000)/10000) + "      " + 
//					((double)Math.round(subValues[a][2]*10000)/10000));
//				
			}
				
			}
		newInt++;
		//sum up the values of each box for each subject, and sum them.
		double sum=0;
		for ( int c=0; c<volumes.length; c++){
			sum=sum+volumes[c];	
		}
		//turn sum values into doubles, add to a final array in the correct position
		sum=(double)Math.round(sum * 100000) / 100000;
		finalValues[k-2]=sum;
	}
newInt=1;
	for (int g=0; g<finalValues.length; g++){
		results.println(" Subject " + (g+1) + " value: " + (finalValues[g]));
		//option to print to the console
		//System.out.println(" Subject " + (g+1) + " value: " + (finalValues[g]));
	}
	summary.close();
	results.close();
}
}










