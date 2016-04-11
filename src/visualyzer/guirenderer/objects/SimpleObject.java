package visualyzer.guirenderer.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SimpleObject {
    private String name;
    private String tier;
    private String mvc;
    private String path;
    private String role;
    
    public static final int TIER_DATA = 1;
    public static final int TIER_LOGIC = 2;
    public static final int TIER_PRESENTATION = 3;
    
    
    public static final int MVC_MODEL = 1;
    public static final int MVC_VIEW = 2;
    public static final int MVC_CONTROLLER = 3;
    
    
    private List<SimpleObject> children;
    private List<String> associations;

    public SimpleObject() {
        this.tier = "0";
        this.mvc = "0";
    }

    public SimpleObject(String name) {
        this.name = name;
        this.tier = "0";
        this.mvc = "0";
    }
    
    public SimpleObject(String name, String tier, String mvc) {
        this.name = name;
        this.tier = tier;
        this.mvc = mvc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTier() {
        return tier;
    }
    
    public String getTierName() {
        if(tier.equals("1,2,3")) return "Presentation Layer,Logic Layer,Data Layer";
        if(tier.replaceAll("\\D", "").length() < 1) return "Undefined";
        int id = Integer.parseInt(tier.replaceAll("\\D", ""));
        switch(id) {
            case TIER_PRESENTATION: return "Presentation Layer";
            case TIER_LOGIC: return "Logic Layer";
            case TIER_DATA: return "Data Layer";
        }
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public String getMvc() {
        return mvc;
    }

    public void setMvc(String mvc) {
        this.mvc = mvc;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<SimpleObject> getChildren() {
        return children;
    }
    
    public int addChild(SimpleObject child) {
        if(children == null) children = new ArrayList<>();
        children.add(child);
        return children.size();
    }

    public void setChildren(List<SimpleObject> childs) {
        this.children = childs;
    }

    public List<String> getAssociations() {
        return associations;
    }
    
    public int addAssociation(String object) {
        if(associations == null) associations = new ArrayList<>();
        associations.add(object);
        return associations.size();
    }

    public void setAssociations(List<String> associations) {
        this.associations = associations;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.name);
        hash = 73 * hash + Objects.hashCode(this.tier);
        hash = 73 * hash + Objects.hashCode(this.mvc);
        hash = 73 * hash + Objects.hashCode(this.path);
        hash = 73 * hash + Objects.hashCode(this.role);
        hash = 73 * hash + Objects.hashCode(this.children);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SimpleObject other = (SimpleObject) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.tier, other.tier)) {
            return false;
        }
        if (!Objects.equals(this.mvc, other.mvc)) {
            return false;
        }
        if (!Objects.equals(this.path, other.path)) {
            return false;
        }
        if (!Objects.equals(this.role, other.role)) {
            return false;
        }
        if (!Objects.equals(this.children, other.children)) {
            return false;
        }
        return true;
    }
    
    public String [] getLocationWithID() {
        String [] mvc = this.mvc.split("[,0 ]");
        String [] tiers = this.tier.split("[,0 ]");
        
        int size = mvc.length + tiers.length;
        int iter = 0;
        String [] rectangles = new String[size];
        
        if(!mvc.equals("0"))
            for(String elem : mvc) {
                rectangles[iter++] = intToID(Integer.parseInt(elem), false);
            }
        
        if(!tiers.equals("0"))
            for(String elem : tiers) {
                rectangles[iter++] = intToID(Integer.parseInt(elem), true);
            }
        
        return rectangles;
    }
    
    private String intToID(int identifier, boolean tiers) {
        if(tiers) {
            switch(identifier) {
                case TIER_DATA: return "data_r";
                case TIER_LOGIC: return "buss_r";
                case TIER_PRESENTATION: return "presn_r";
            }
        }
        else {
            switch(identifier) {
                case MVC_MODEL: return "model_r";
                case MVC_VIEW: return "view_r";
                case MVC_CONTROLLER: return "cont_r";
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return name;
    }
}
