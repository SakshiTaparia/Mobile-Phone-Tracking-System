import java.util.*;

// This class maintains a Tree structure of all levels of mobile phone stations as well as the mobile phones stored in each.

class RoutingMapTree
{

// The tree also contains a Mobile phone set corresponding to it.
// Switched off phones are kept in the set ans removed from the tree.
// When phone is switched on it is registered with a base station again and removed from this set.
	public MobilePhoneSet PhonesSwitchedOff = new MobilePhoneSet();
	public Exchange root;

// constructor assigns identifier zero to the root Exchange of the tree.
	public RoutingMapTree ()
	{
		root = new Exchange (0);
		root.parent = null;
	}

// This method tells whether a given exchange is a part of the tree or not
	public Boolean containsNode(Exchange a)
	{
// B is the pointer to the head of the childlist of root
		LinkedListNode<Exchange> b;

		if (root.Childrenlist == null) 
			b=null;

		else b = root.Childrenlist.gethead();

		if (root == a) {return true;}
		else
		{
			while (b!=null)
			{
				if (b.getelement() == a) return true;

// Function is called recursively taking a child as the new root of the corresponding subtree
				RoutingMapTree recursion = new RoutingMapTree();
				recursion.root.Childrenlist = b.getelement().Childrenlist;
				recursion.containsNode(a);

				if (recursion.containsNode(a) != false) 
					{return true;}

				b = b.getnext();
			}
		}
		return false;
	}

// This method tells whether a given mobile phone exists or not
	public boolean containsPhone(MobilePhone a)
	{
// if phone exists and is switched off, it will be a member of the PhoneSwitchedOff set
		if (PhonesSwitchedOff.IsMember(a)) 
			return true;

// if phone is on, it will be registered in the base station and will definitely be present in the Mobile Phones set of root
// which is the union of Mobile Phone sets of all stations.
		if (root.Mobileset.IsMember(a)) 
			return true;

		return false;
	}

// This method returns the exchange given an identifier using a similar recursive algorithm
	public Exchange GetExchange (Integer a)
	{
	    LinkedListNode<Exchange> b;

// b is used to access the head of the children list of the root
		if (root.Childrenlist == null) 
			b=null;

		else 
			b = root.Childrenlist.gethead();

		if (root.Id ==a) {return root;}
		else
		{
			while (b!=null)
			{
				if (b.getelement().Id == a) return b.getelement();

// Function is called recursively taking a child as the new root of the corresponding subtree
				RoutingMapTree recursion = new RoutingMapTree();
				recursion.root.Childrenlist = b.getelement().Childrenlist;
				if (recursion.GetExchange(a) != null) 
					return recursion.GetExchange(a);

				b = b.getnext();
			}
		}
// It returns null if no such Exchange exists
		return null;
	}

// This method adds Exchange b to the childlist of Exchange a
	public void AddExchange( Exchange a, Exchange b)
	{
		a.Childrenlist.InsertRear(b);
		a.Mobileset = a.Mobileset.UnionMobiles(b.Mobileset);

// Update the whole tree
		b.parent = a;
		Exchange temp = a;
		while (temp.parent != null) 
			{temp.parent.Mobileset.UnionMobiles(b.Mobileset); 
				temp = temp.parent;} 

		return;
	}

// This method returns the Mobile phone given its identifier.
	public MobilePhone GetMobilePhone (Integer num) 
	{
// Searching mobile Phone set of root
		LinkedListNode <MobilePhone> head = root.Mobileset.set.gethead();
		while (head != null)
		{
			if ( (int) head.getelement().number() == (int) num) return head.getelement();
			head = head.getnext();
		}

// Searching the Switched off mobile phone set.
		head = PhonesSwitchedOff.set.gethead();
		while (head != null)
		{
			if ((int) head.getelement().number == (int) num) return head.getelement();
			head = head.getnext();
		}
		
// If phone does not exist it returns null
		return null;
	}

// This method switches On a mobile phone a at a base station b assuming a exists and b is a base station
	public void switchOn(MobilePhone a, Exchange b) throws Exception
	{
		if (b.Childrenlist.gethead() != null) {throw new Exception("Error - Exchange with identifier" + b.Id + " is not a base station.");}
		
// If phone was off, remove it from Phone switched off set and switch it on
		if (a.status() == false) 
			PhonesSwitchedOff.Delete(a);

		a.switchOn();

// register it with base station b
		b.Mobileset.Insert(a);
		a.basestation = b;

// Update the tree
		while (b.parent != null) 
			{b.parent.Mobileset.Insert(a); 
				b = b.parent;}
		
		return;
	}

// This method switches off a phone that is currently registered in the tree
	public void switchOff(MobilePhone a) throws Exception
	{
		if (a.status() == false){throw new Exception("Error - Mobile phone with identifier " + a.number +" is currently switched off");}

		else 
		{
// remove a from base station and upadate the tree
			a.basestation.Mobileset.Delete(a);

			while (a.basestation.parent != null) 
				{a.basestation.parent.Mobileset.Delete(a); 
					a.basestation = a.basestation.parent;}

// Insert a in the phone switched off set
			a.switchOff();
			a.basestation = null;
		    PhonesSwitchedOff.Insert(a);
	    }
		return;		
	}

// This method returns the base station with which a phone is registered if switched on and throws exception if the phone is off
	public Exchange findPhone(MobilePhone m) throws Exception
	{
		return m.location();
	}


// This method returns the lowest Exchange that contains all the mobile phones of both the base stations a and b 
	public Exchange lowestRouter(Exchange a, Exchange b) throws Exception
	{
		if (a.Childrenlist.gethead() != null) {throw new Exception("Error - Exchange with identifier " + a.Id +" is not a base station");}
		if (b.Childrenlist.gethead() != null) {throw new Exception("Error - Exchange with identifier " + b.Id +" is not a base station");}

// Loop ends when both a and b reach a common parent

		RoutingMapTree subtree = new RoutingMapTree();
		subtree.root.Childrenlist = a.Childrenlist;

		while (subtree.containsNode(b) == false)
		{
			a = a.parent;
			subtree.root.Childrenlist = a.Childrenlist;
		}

		return a;
	}


// This method returns a list of exchanges starting from the base station where a is registered to the base station where b is registered
// and represents the shortest route in the routing map tree between the two base stations
	public ExchangeList routeCall(MobilePhone a, MobilePhone b) throws Exception
	{
		Exchange A = this.findPhone(a);
		Exchange B = this.findPhone(b);

		ExchangeList routeA = new ExchangeList();

// If base stations of a and b are same then only the base station is returned
		if (A.Id == B.Id)
		{		
			routeA.InsertFront(A);
			return (routeA);
		}

// List 1 goes from A to the lowest router
		Exchange temp = A;
		while( temp.Id != this.lowestRouter(A,B).Id )
		{
			routeA.InsertRear(temp);
			temp = temp.parent;
		}

		routeA.InsertRear(this.lowestRouter(A,B));

// List 1 goes from A to the lowest router		
		temp = B;
		ExchangeList routeB = new ExchangeList();

		while( temp.Id != this.lowestRouter(A,B).Id )
		{
			routeB.InsertFront(temp);
			temp = temp.parent;
		}

// Merging the two lists to give a single list of Exchanges
		while (routeB.isEmpty() == false)
			routeA.InsertRear(routeB.DeleteFront());

		return (routeA);
	}

// This method removes a mobile phone from the current base station and registers it with the new one
    public void movePhone(MobilePhone a, Exchange b) throws Exception
    {
    	this.switchOff(a);
    	this.switchOn(a,b);
    }


// This method answers the queries of the user by calling the appropritate functions
// and return the output in the form a string

	public String performAction(String actionMessage) 
	{
// Splitting the input string at spaces
		String [] splitwords = actionMessage.split(" ");

		try{

		if (splitwords[0].equals("addExchange"))
		{
			this.addExchange(Integer.valueOf(splitwords[1]), Integer.valueOf(splitwords[2]));
			return ("");
		}

		else if (splitwords[0].equals("switchOnMobile"))
		{
			this.switchOnMobile(Integer.valueOf(splitwords[1]), Integer.valueOf(splitwords[2]));
			return ("");
		}

		else if (splitwords[0].equals("switchOffMobile"))
		{
			this.switchOffMobile(Integer.valueOf(splitwords[1]));
			return ("");
		}

		else if (splitwords[0].equals("queryNthChild"))
		{	
			return (this.queryNthChild(Integer.valueOf(splitwords[1]), Integer.valueOf(splitwords[2])));
		}

		else if (splitwords[0].equals("queryMobilePhoneSet"))
		{
			return (this.queryMobilePhoneSet(Integer.valueOf(splitwords[1])));
		}

		else if (splitwords[0].equals("findPhone"))
		{	
			return (this.queryFindPhone(Integer.valueOf(splitwords[1])));
		}

		else if (splitwords[0].equals("lowestRouter"))
		{	
			return (this.queryLowestRouter(Integer.valueOf(splitwords[1]), Integer.valueOf(splitwords[2])));
		}		

		else if (splitwords[0].equals("movePhone"))
		{	
			this.queryMovePhone(Integer.valueOf(splitwords[1]), Integer.valueOf(splitwords[2]));
			return ("");
		}

		else if (splitwords[0].equals("findCallPath"))
		{
		    return (this.queryFindCallPath(Integer.valueOf(splitwords[1]), Integer.valueOf(splitwords[2])));	
		}

		else
		{
			return ("Query message not identified");
		}

		}
		
		catch (Exception e)
		{
			System.out.println(e.toString());
		}
// try and catch the user defined expections in all the methods called.

		return ("");
	}

// This method creates a new Exchange with identifer b and adds it to the childlist of an already 
//existing Exchange on the tree with identifier a
	public void addExchange( Integer a, Integer b) throws Exception
	{
// Get the exchange corresponding to identifier a
		Exchange A = GetExchange(a);
		if (A==null) {throw new Exception("Error - No exchange with identifier " + a + " found in the network");}

// Create a new Exchange corresponding to identifier b
		Exchange B = new Exchange(b);

// call Add Exchange method
		this.AddExchange(A,B);
		return;
	}

// Switches on mobile phone with identifier a at the base station with identifier and creates a new phone
// if no phone with identifier a exists 
	public void switchOnMobile (Integer a, Integer b) throws Exception
	{
		MobilePhone A;

// if no phone with identifier a exists, create a new one
		if (GetMobilePhone(a) == null) {A = new MobilePhone (a);}
		else A = GetMobilePhone(a);

		Exchange B = GetExchange(b);
		if (B==null) {throw new Exception("Error - No exchange with identifier " + b + " found in the network");}

// calls the switchOn method
		this.switchOn(A,B);

		return;
	}

// // This method switches off the phone with identifier a that is currently registered in the tree
	public void switchOffMobile (Integer a) throws Exception
	{
		MobilePhone A = GetMobilePhone(a);
		if (A == null) {throw new Exception("Error - No mobile phone with identifier " + a + " found in the network");}

		this.switchOff(A);
		return;
	}

// This method returns the identifier of the bth child of the Exchange with identifier a
	public String queryNthChild (Integer a, Integer b) throws Exception
	{
	    String answer;

		Exchange A = GetExchange(a);
		if (A==null) {throw new Exception("Error - No exchange with identifier " + a + " found in the network");}

		answer = "queryNthChild " + String.valueOf(a) + " " + String.valueOf(b) + ": " + String.valueOf(A.child(b).Id);
		return answer;
	}

// This method returns all identifiers of all the mobile phones registered with the Exchange with identifier a
	public String queryMobilePhoneSet (Integer a) throws Exception
	{
	    String answer = "queryMobilePhoneSet " + String.valueOf(a) + ": ";

		Exchange A = GetExchange(a);
		if (A==null) {throw new Exception("Error - No exchange with identifier " + a + " found in the network");}

		LinkedListNode <MobilePhone> b = A.Mobileset.set.gethead();

// Adding the identifiers in the answer string
		if (b==null) return answer;

            while (b.getnext()!=null)
			{
				answer = answer + String.valueOf(b.getelement().number()) + ", ";
				b = b.getnext();
			}
			answer = answer + String.valueOf(b.getelement().number());

		return answer;
	}

// This method returns the base station with which the phone with identifier a is registered 
// and throws exception if the phone is off or does not exist
    public String queryFindPhone (Integer a) throws Exception
    {
    	String answer = "queryFindPhone " + String.valueOf(a) + ": "; 

    	MobilePhone A = GetMobilePhone(a);
    	if (A==null) {throw new Exception("Error - No mobile phone with identifier " + a + " found in the network");}

    	Exchange ExA = findPhone(A);

// Converting the output to a string
    	answer = answer + String.valueOf(ExA.Id);
    	return answer;
    }

// This method returns the lowest Exchange that contains all the mobile phones of both the base stations with identifiers a and b 
    public String queryLowestRouter (Integer a, Integer b) throws Exception
    {
    	String answer = "queryLowestRouter " + String.valueOf(a) + " " + String.valueOf(b) + ": ";

    	Exchange A = GetExchange(a);
    	if (A==null) {throw new Exception("Error - No exchange with identifier " + a + " found in the network");}

    	Exchange B = GetExchange(b);
    	if (B==null) {throw new Exception("Error - No exchange with identifier " + b + " found in the network");}

// Converting the output to a string
    	answer = answer + String.valueOf(lowestRouter(A,B).Id);
    	return answer;
    }

// This method returns a list of exchanges from the base station containing phonw with identifier a to the base station containing
// phone with identifier b and represents the shortest route between the two base stations in the tree
    public String queryFindCallPath (Integer a, Integer b) throws Exception
    {
    	String answer = "queryFindCallPath " + String.valueOf(a) + " " + String.valueOf(b) + ": ";

    	MobilePhone A = GetMobilePhone(a);
    	if (A==null) {throw new Exception("Error - No mobile phone with identifier " + a + " found in the network");}

    	MobilePhone B = GetMobilePhone(b);
    	if (B==null) {throw new Exception("Error - No mobile phone with identifier " + b + " found in the network");}

    	ExchangeList path = routeCall(A,B);

// Converting the output to a string
    	LinkedListNode <Exchange> head = path.gethead();

    	while(head.getnext() != null)
    	{
    		answer = answer + String.valueOf(head.getelement().Id) + ", ";
    		head = head.getnext();
    	}

    	answer = answer + String.valueOf(head.getelement().Id);
    		return answer;
	}

// This method removes the mobile phone with identifier a from the current base station and registers it with the one with identifier b
    public void queryMovePhone (Integer a, Integer b) throws Exception
    {
        MobilePhone A = GetMobilePhone(a);
		if (A == null) {throw new Exception("Error - No mobile phone with identifier " + a + " found in the network");}

		Exchange B = GetExchange(b);
		if (B == null) {throw new Exception("Error - No mobile phone with identifier " + b + " found in the network");}	

		movePhone(A, B);
    }
}
