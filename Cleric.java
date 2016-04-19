import java.util.Random;

public class Cleric {
	string name;
	final int maxhp;
	int hp;
	final int maxmp;
	int mp;
	
	
}

public void selfAid(){
	System.out.println(this.name +"はセルフエイドを唱えた");
	this.hp = this.maxhp;
	this.mp -= 5;
	System.out.println(this.name +"はHPが最大まで回復した");
}

public void pray(int sec){
	System.out.println(this.name +"は祈った");
		int recover = new Random().nextInt(3) + sec;
		
		int recoverActual = Math.min(this.maxmp - thismp, recover);
		
		thismp += recoverActual;
		
		System.out.println("MPが"+ recoverActual +"回復した");
		
		return recoverActual;
	
}