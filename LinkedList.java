import java.util.*;

// This class implements the node used to construct the linked list

class LinkedListNode <T>
{

// T is used for generic method declaration
	private T element;
	private LinkedListNode <T> next;

// Constructor of the class
	public LinkedListNode ( T element, LinkedListNode <T> next)
	{
		this.element = element;
		this.next = next;
	}

// Getter and setter functions
	public T getelement () 
	{return element;}

	public LinkedListNode <T> getnext () 
	{return next;}

	public void setelement ( T e ) 
	{ element =e;}

	public void setnext ( LinkedListNode <T> n ) 
	{ next =n;}
}



// This class implements a singly linked list using the node class defined earlier

class LinkedList <T>
{

// references for head and tail variables of the linked list
	public LinkedListNode <T> head;
	public LinkedListNode <T> tail;

	public LinkedList () {head = null; tail = null;}

	public LinkedListNode <T> gethead() 
	{return head;}

	public LinkedListNode <T> gettail() 
	{return tail;}

// This method returns true is the list in empty i.e. both head and tail are null and returns false otherwise
	public boolean isEmpty() 
	{return tail == null;} 

// This method checks if the given Object e is an element of the linked list
	public boolean isMember (T e)
	{
		LinkedListNode <T> temp = head;

		while(temp != null) 
			{if (temp.getelement() == e) return true; temp = temp.getnext();}

		return false;	
	}

// This method inserts a new element in the beginning of the list
	public void InsertFront (T e) 
	{
		head = new LinkedListNode <T> (e, head);

// If the list is initially empty
		if (tail == null)
			tail = head;
	}

// This method inserts a new element at the end of the list
	public void InsertRear(T a)
	{
		LinkedListNode <T> node = new LinkedListNode <T> (a, null);

// If the list is initially empty
		if (tail == null) 
			{tail= node; head = node; return;}

		else 
			{tail.setnext(node); tail = node; return;}
	}

// This method deletes the node of the list with element e and throws an exception if no such node exists 
	public void DeleteT (T e)
	{
		if (this.isMember(e) == false) return;

// When the list has a single node
		if (head == tail) 
			{head = null; tail = null; return;} 

// When the head itself needs to be deleted
		if (head.getelement() == e) 
			{head = head.getnext(); return;}

// When a general node or tail node has to be deleted
		LinkedListNode <T> temp = head;
		while (temp.getnext().getelement() != e) 
			temp = temp.getnext();

		temp.setnext(temp.getnext().getnext());
		if (temp.getnext() == null) 
			tail = temp;

		return;
	}
}

