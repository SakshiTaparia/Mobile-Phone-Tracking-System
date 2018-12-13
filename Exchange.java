import java.util.*;

// ExchangeList is a singly linked list with the Objects of the type Exchange.

class ExchangeList extends LinkedList <Exchange> 
{
	public Exchange DeleteFront()
	{
		LinkedListNode<Exchange> temp = head;

		if (head == tail)
		tail = null;

		head = head.getnext();
		return temp.getelement();
	}
}



// Exchange class represents mobile stations and forms the nodes of the Routing Map tree.

class Exchange
{

// Id is the unique identifier of the exchange.
// Childrenlist is an exchange list storing all the children nodes of the exchange.
	public Integer Id;
	public Exchange parent;
	public MobilePhoneSet Mobileset = new MobilePhoneSet();
	public ExchangeList Childrenlist = new ExchangeList();

// Constructor
	public Exchange (Integer number) 
	{Id = number; parent = null;}

// This method tells if a given exchange is the root of the tree i.e. has no parent or not.
	public Boolean isRoot() 
	{return parent == null;}

// This method tells if a given exchange is the leaf of the tree i.e. has no children or not.
	public Boolean isLeaf() 
	{return Childrenlist == null;}

// This method returns the number of children of an Exchange node.
	public Integer numChildren() 
	{
		Integer count = 0;
		LinkedListNode <Exchange> pointer = Childrenlist.gethead();
		while (pointer != null) 
		{
			count ++;
			pointer = pointer.getnext();	
		}
		return count;
	}

// This method returns the ith child of the nose i.e. the ith element of childlist.
	public Exchange child (Integer i) throws Exception 
	{
		if (i> this.numChildren()) {throw new Exception("Error - Value of " + i +" exceeds the number of children.");}

		else
		{
			LinkedListNode <Exchange> pointer = Childrenlist.gethead();

// Head is the zeroth element.
		    Integer x = 0;
		    while (x<i)
		    {
			    pointer = pointer.getnext();
			    x++;
		    }
		    return pointer.getelement();
		}
		
	}

// This method returns the subtree corresponding to the ith child.
	public RoutingMapTree subtree(Integer i) throws Exception 
	{
		if (i> this.numChildren()) {throw new Exception("Error - Value of " + i +" exceeds the number of children.");}

		else
		{
			LinkedListNode <Exchange> pointer = Childrenlist.gethead();
		    Integer x = 1;
		    while (x<i)
		    {pointer = pointer.getnext(); x++;}

		    RoutingMapTree subtree = new RoutingMapTree ();

// Make the ith child as the root of the Router tree called subtree ans return it
		    subtree.root.Childrenlist = pointer.getelement().Childrenlist;
		    return (subtree);
		}
	}
}

