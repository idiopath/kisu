package kisu;

public class Start {

	public static void main(String[] args) {

//		Killersudoku k = new Killersudoku("aaabcccddeafbggghheefbijjhhfffiijkhhlllijjkmmnllokkkpmnqqokrrppssstkrpputtttkppuu"); //min 11 max 45 876543210542 funkt
//		Killersudoku k = new Killersudoku("aaabcccddeafbggghheefbijjhhfffiijkhhlllijjkmmnllokkkpmnqqokrrppssstkrpputtttkppuu", "41200000003");
//		Killersudoku k = new Killersudoku("aaabcdddeafbbccceegffhiijjegkkhiljmennhholmmennhoolmppqhhrolsppqqrrrlsstqqqruustt", "123456789456789123789123456214365897365897214897214365542631978631978542978542631");
//		Killersudoku k = new Killersudoku("aabbbccccdaaeeeeccdaafgehhidajfgkhlidjjfgkmlidjjggnmmmdoogpnnnmdoqpprrnmssqpprnnn");

		long time = System.currentTimeMillis();
		Killersudoku k = new Killersudoku("aaabcccdefaabccgdefhijjkglefhiijkglemhinjkkkemmnnooppeqqqroosppttrrrospptrrooospp");		
//		
		System.out.println("Genriert in " + (System.currentTimeMillis() - time) + " Millisekunden");
//		
////		k.set("a1a2a3b4b5c6d7d8d9a4a5e6f7b8c9d1g2d3h7h8e9f1c2c3c4g5d6i2h1j4f3c7k5l6l9l8i3i6j5f9f4k8k2l1l7m8m9j7j2j6j1n5n3n4m5m7o1o8p3p4q9n6n2m6r4r8o5o9p2q3s7n1r9r3o2o6t1t7t8s4s5");
//		
		k.verify();
		
		System.out.println("Und verifiziert in insgesamt " + (System.currentTimeMillis() - time) + " Millisekunden");
		System.out.println("Frames: " + k.getFrames());
		System.out.println("Values: " + k.getValues());
		
//		System.out.println(Integer.toBinaryString(a[i]) + ": lead " + Integer.numberOfLeadingZeros(a[i]) + " trail " + Integer.numberOfTrailingZeros(a[i]));
		
//		k.generate();
	}

}
/*
Abbruch nach 10 Millionen Versuchen f�r generateValue...
Frames: aaabcccddeafbggghheefbijjhhfffiijkhhlllijjkmmnllokkkpmnqqokrrppssstkrpputtttkppuu

Frames: abbbcccddaaabecccfgaabeehifgjjbkkhifgllllkmffggnoppmmqrgnooppmqrgnnnnnmqggssssmmq min 11 max 45 876543210542 funktioniert wohl nich

876543210543010210210210210352111000200000000000000000999999999999999999999999999 10000000 min 27 max 53
Frames: aaabcccdefaabccgdefhijjkglefhiijkglemhinjkkkemmnnooppeqqqroosppttrrrospptrrooospp

Perfekt! Genau 1 L�sung :-)
Frames: abbccddeeaffccggghaffffijghaaffkijjhalmnkkkohalmnnpkoollqnrpssoltqrrrruolttttttuu
Values: 123456789456789123789123456264315897315897264978264315531948672642571938897632541

Perfekt! Genau 1 L�sung :-)
Und verifiziert in insgesamt 2173 Millisekunden
Frames: abbbcddefagggcceefhggijjekfhhiijjjkklliimmmknooipqqqnnoorpppqssoortuvqsswwrtuvqxx
Values: 123456789456789123789123456214365897365897214897214365531648972642971538978532641

Durchg�nge f�r 81 Werte: 339
Mist! Mehr als 1 L�sung, check die Unterschiede:
Value: 123456789456789123789123456214365978365897214978241365591632847642978531837514692
Sol 1: 123456789456789123789123456214365978365897214978241365591632847642978531837514692
Sol 2: 123456789456789123789123456214635978365897214978241365591362847642978531837514692
Diff: 30 31 57 58 

Perfekt! Genau 1 L�sung :-)
Und verifiziert in insgesamt 18397 Millisekunden
Frames: aaabcccddeafbggghheefbijjhhfffiijkhhlllijjkmmnllokkkpmnqqokrrppssstkrpputtttkppuu
Values: 412356789356789124789124356123465897548971632697832541235697418971548263864213975

*/