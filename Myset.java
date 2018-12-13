import java.util.*;

// This class implements Sets using Singly Linked list

class Myset <T>
{
	public LinkedList <T> set = new LinkedList<T>();
	public Myset () {}

// This method returns true is the set and hence, the corresponding linked list is empty and returns false otherwise
	public boolean IsEmpty () 
	{return set.isEmpty();}

// This method checks if the given Object e is a member of the set
	public Boolean IsMember(T o) 
	{return set.isMember(o);}

// This method inserts a new element in the set
	public void Insert(T o)
	{
// If the member already exists, it will not be added again in the set
		if (set.isMember(o) == true) return;
		else set.InsertRear(o);
	}

// This method deletes the element e of the set and throws an exception if no such element exists 
	public void Delete(T o)
	{
		if (set.isMember(o)) {set.DeleteT(o);}
	}

// This method returns the union of the given set with another set a
    public Myset<T> Union(Myset<T> a)
    {
    	Myset <T> union = new Myset<T>();
    	union = a;

    	LinkedListNode <T> pointer = set.gethead();
    	while (pointer != null) 
    		{union.Insert(pointer.getelement()); pointer = pointer.getnext();}

    	return union;
    }

// This method returns the intersection of the given set with another set a
    public Myset<T> Intersection(Myset<T> a)
    {
    	Myset <T> intersection = new Myset<T>();

    	LinkedListNode <T> pointer = set.gethead();
    	while (pointer != null) 
    		{
    			if (a.IsMember(pointer.getelement())) 
    			intersection.Insert(pointer.getelement()); 

    			pointer = pointer.getnext();
    		}
    	return intersection;
    }
}



// This class defines the Object corresponding to a MobilePhone

class MobilePhone
{
	public Integer number;
	public boolean status = true;
// true represents On and false represents Off

	public Exchange basestation;

// Constructor of the class takes a unique identifier for every MobilePhone object
	public MobilePhone(Integer number) {this.number = number;}

// Getter and setter functions
	public Integer number() 
	{return number;}

	public Boolean status() 
	{return status;}

	public void switchOn() 
	{if (status == false) status = true;}

	public void switchOff() 
	{if (status == true) status = false;}

// This method returns the base station i.e. the lowest level exchange with with the phone is registered if switched on
// and throws an exception if the phone is Off
	public Exchange location() throws Exception
	{
		if (status == true)
			return basestation;
		else {throw new Exception("Error - Mobile phone with identifier " + number +" is currently switched off");}
	}
}



// MobilePhoneSet extends Myset class such that the elements are of the type MobilePhone

class MobilePhoneSet extends Myset <MobilePhone> 
{
	public MobilePhoneSet UnionMobiles( MobilePhoneSet a)
    {
    	MobilePhoneSet union = new MobilePhoneSet();
    	union = a;

    	LinkedListNode <MobilePhone> pointer = set.gethead();
    	while (pointer != null) 
    		{union.Insert(pointer.getelement()); pointer = pointer.getnext();}
    	return union;
    }
}