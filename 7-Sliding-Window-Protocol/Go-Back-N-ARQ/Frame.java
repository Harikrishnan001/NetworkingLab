public class Frame{
	final int number;
	final String data;
	boolean isAck=false;
	
	public Frame(int number,String data)
	{
		this.number=number;
		this.data=data;
	}
}