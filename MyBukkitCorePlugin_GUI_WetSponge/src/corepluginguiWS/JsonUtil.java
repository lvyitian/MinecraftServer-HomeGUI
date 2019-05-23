package corepluginguiWS;

import java.util.ArrayList;

public class JsonUtil {
  public ArrayList<Home> HomeList=new ArrayList<Home>();
  public class Home{
	public Home(String string, ArrayList<HomeLocation> arrayList) {
		this.Player=string;
		this.LocationList=arrayList;
	}
	  public String Player="";
	  public ArrayList<HomeLocation> LocationList=new ArrayList<HomeLocation>();
	  public class HomeLocation{
		 public HomeLocation(long i, String World, ArrayList<Double> templ) {
			this.Index=i;
			this.World=World;
			this.Location=templ;
		}
		  public long Index=-1;
		  public String World="";
		  public ArrayList<Double> Location=new ArrayList<Double>();
	  }
  }
}
