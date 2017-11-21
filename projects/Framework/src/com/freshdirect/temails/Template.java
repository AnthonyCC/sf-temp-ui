package com.freshdirect.temails;



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;


public class Template implements ParserI {

	private String id;
	private String name;
    private String eid;
	private List parsers;
	private String subsystem;
	private String formattedContent=null;

	public Template() {
		this.parsers = new ArrayList();
	}

	

	public Template(String id, String name, String eId, ParserI condition) {
		this();
		this.id = id;
		this.name = name;		
		this.eid=eId;
		this.parsers.add(condition);	
	}

	
	/** Orders products by department & full name */
	public final static Comparator DEPTFULL_COMPARATOR = new Comparator() {
		
		@Override
        public int compare(Object obj1, Object obj2) {
			try {
				ParserI f1 = ((ParserI) obj1);
				ParserI f2 = ((ParserI) obj2);
				
				System.out.println(" ParserI f1:"+f1);
				System.out.println(" ParserI f2:"+f2);
				System.out.println(" ParserI f1.order:"+f1.getOrder());
				System.out.println(" ParserI f2.order:"+f2.getOrder());
				if (Integer.parseInt(f1.getOrder()) >  
				Integer.parseInt(f2.getOrder())) {
					
					return 1;
				} else if (Integer.parseInt(f1.getOrder()) < 
				Integer.parseInt(f2.getOrder()))  {
					
					return -1;
				} else {
					return 0;
				}
			} catch (NumberFormatException fdre) {
				// rethrow as a runtime exception
				throw new RuntimeException("order is not set in Formater of email template"+fdre.getMessage());
			} 
		}
	
	};
	
		@Override
        public String parse(Object target, TemailRuntimeI ctx) {
			//Check that the rule has not expired
			this.formattedContent="";
		     Collections.sort(this.parsers,Template.DEPTFULL_COMPARATOR);
			String s = null;
			int index=0;
			for (Iterator i = this.parsers.iterator(); i.hasNext();) {
				ParserI c = (ParserI) i.next();
				if(index>0){
					if(formattedContent!=null && formattedContent.trim().length()>0) formattedContent=formattedContent+"&&";
				    formattedContent=formattedContent+c.parse(target, ctx);
				}else { formattedContent=c.parse(target, ctx); }
				index++;								 								
			}
			return this.formattedContent;
	}

	@Override
    public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	



	
	public String getSubsystem() {
		return this.subsystem;
	}

	public void setSubsystem(String subsystem) {
		this.subsystem = subsystem;
	}

	

	@Override
    public String toString() {
		return "Template[" + id + ", '" + name + "', " +""  + ", " + eid + "]";
	}

	
	public Template deepCopy() {
        // do a serialization / de-serialization cycle as a trick
        // against explicit deep cloning

        // serialization
        ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
        ObjectOutputStream    oas  = null;
        try {
            oas = new ObjectOutputStream(baos);
            oas.writeObject(this);
            oas.close();
        } catch (IOException e) {
            return null;
        }

        // de-serialization
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream    oin = null;
        try {
            oin = new ObjectInputStream(bais);
            return (Template) oin.readObject();
        } catch (ClassNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }		
	}

	
	

	public String getEid() {
		return eid;
	}

	public void setEid(String id) {
		eid = id;
	}

	public List getParsers() {
		return parsers;
	}
	
	
	

	public void setParsers(List parsers) {
		/*
		  List tmpChildList=new ArrayList();	
		  List tmpparentList=new ArrayList();
		  
		for(int i=0;i<this.parsers.size();i++){
			ParserI tmpP=(ParserI)parsers.get(i);
			if(tmpP.getParentId()!=null && tmpP.getParentId().trim().length()>0){
			
					tmpparentList.add(tmpP);
			}else{
					tmpChildList.add(tmpP);
			}						
		}		
		for(int j=0;j<tmpChildList.size();j++){
			
			ParserI tmpP=(ParserI)tmpChildList.get(j);
			
			for(int i=0;i<tmpparentList.size();i++){
				ParserI parentP=(ParserI)tmpparentList.get(i);
				if(parentP.getParentId().equalsIgnoreCase(tmpP.getId())){
					tmpP.setChildParser(parentP);
					System.out.println("setting childParser id for  :"+tmpP.getId()+" and child Parser Id is:"+parentP.getId());
				}
			}			
		}		*/
		this.parsers = parsers;
	}
	
	private List tmpParserList=new ArrayList();
	
	public void addParser(ParserI condition) {
		
		if(condition.getParentId()!=null && condition.getParentId().length()>0){
			boolean isParentFound=false;
			for(int i=0;i<this.parsers.size();i++){
				ParserI tmpP=(ParserI)parsers.get(i);
				if(tmpP.getId().equalsIgnoreCase(condition.getParentId())){
					tmpP.setChildParser(condition);					
					isParentFound=true;
				}
			}
			if(!isParentFound){ System.out.println("no Parent Found from List :"+condition.getParentId() );  this.tmpParserList.add(condition);  }
		}
		else{
			
			if(condition.getId()!=null && condition.getId().trim().length()>0)
			{
				for(int i=0;i<this.tmpParserList.size();i++){
					ParserI tmpP=(ParserI)tmpParserList.get(i);
					if(tmpP.getParentId().equalsIgnoreCase(condition.getId())){
						condition.setChildParser(tmpP);						
						tmpParserList.remove(tmpP);						
					}
				}								   
			} 
			this.parsers.add(condition);
		}   
	}
	

	@Override
	public String getOrder() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public void setOrder(String order) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		boolean valid = true;
		for (Iterator i = this.parsers.iterator(); i.hasNext();) {
			ParserI c = (ParserI) i.next();
			valid &= c.validate();
		}
		return valid;
	}



	@Override
	public String getParentId() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public void setChildParser(ParserI p) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public ParserI getChildParser() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public int getLevel() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}	