package ExtraUtils.DataStructure;

/**
 * Created by linzhang on 12/8/2015.
 */
public class DataObject extends Object{
    private String id;
    private String name;

    public DataObject(String id)
    {
        this.id = id;
    }

    public DataObject(String id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public String getID() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
}
