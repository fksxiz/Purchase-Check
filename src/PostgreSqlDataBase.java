
import java.util.Properties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class PostgreSqlDataBase {

    private String host;
    private String port;
    private String database;
    private String user;
    private String password;


    PostgreSqlDataBase(String host, String port, String database, String user, String password) {
        regDriver();
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
    }


    public static void regDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Connection getConncetion() {
        try {
            String url = "jdbc:postgresql://" + getHost() + ":" + getPort() + "/" + getDatabase();
            Properties autharization = new Properties();
            autharization.put("user", getUser());
            autharization.put("password", getPassword());
            Connection connection = DriverManager.getConnection(url, autharization);
            return connection;
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    private Statement getStatement(Connection connection) {
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            return statement;
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public ResultSet execQuery(String query) {
        ResultSet result = null;
        Connection connection = getConncetion();
        if (connection != null) {
            Statement statement = getStatement(connection);
            if (statement != null) {
                try {
                    result = statement.executeQuery(query);
                } catch (Exception throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        try {
            connection.close();
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
    /*public static Statement getStatement(String url,String user, String password){
        try{
            Class.forName("org.postgresql.Driver");
            Properties autharization = new Properties();
            autharization.put("user",user);
            autharization.put("password",password);
            Connection connection = DriverManager.getConnection(url,autharization);
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            return statement;
        }
        catch(Exception ex){
            System.out.println("Connection failed...");
            System.out.println(ex);
            return null;
        }
    }

    public static List<String> getDataForYear(int year, Statement statement){
        try {
            ResultSet table = statement.executeQuery("SELECT reciepts.id, rooms.number,services.service, services.price, reciepts.quantity, units.unit_name, reciepts.date, reciepts.year " +
                    "FROM reciepts " +
                    "INNER JOIN services ON(services.id=reciepts.service_id) " +
                    "INNER JOIN rooms ON(rooms.id=reciepts.room_id) " +
                    "INNER JOIN units ON(units.id=reciepts.unit_id) " +
                    "WHERE year="+year);
            List<String> colName=new ArrayList<>();
            List<String> data=new ArrayList<>();
            table.first();
            for(int j=1;j<=table.getMetaData().getColumnCount();j++){
                colName.add(table.getMetaData().getColumnName(j));
            }
            table.beforeFirst();
            String buf;
            while (table.next()){
                buf = "";
                for(int j =1;j<=table.getMetaData().getColumnCount();j++){
                    buf+=table.getString(j)+"/";
                }
                data.add(buf);
            }
            if(table!=null){table.close();}
            return data;
        }catch (Exception e){
            return null;
        }

    }

    public static List<String> getDataForYearAndRoom(int year,int room_number, Statement statement){
        try {
            ResultSet table = statement.executeQuery("SELECT reciepts.id, rooms.number,services.service, services.price, reciepts.quantity, units.unit_name, reciepts.date, reciepts.year " +
                    "FROM reciepts " +
                    "INNER JOIN services ON(services.id=reciepts.service_id) " +
                    "INNER JOIN rooms ON(rooms.id=reciepts.room_id) " +
                    "INNER JOIN units ON(units.id=reciepts.unit_id) " +
                    "WHERE year="+year+" and rooms.number="+room_number);
            List<String> colName=new ArrayList<>();
            List<String> data=new ArrayList<>();
            table.first();
            for(int j=1;j<=table.getMetaData().getColumnCount();j++){
                colName.add(table.getMetaData().getColumnName(j));
            }
            table.beforeFirst();
            String buf;
            while (table.next()){
                buf = "";
                for(int j =1;j<=table.getMetaData().getColumnCount();j++){
                    buf+=table.getString(j)+"/";
                }
                data.add(buf);
            }
            if(table!=null){table.close();}
            return data;
        }catch (Exception e){
            return null;
        }
    }
    public static List<String> getServices(Statement statement){
        try {
            ResultSet table = statement.executeQuery("SELECT services.service FROM services");
            List<String> data=new ArrayList<>();
            table.first();
            table.beforeFirst();
            String buf;
            while (table.next()){
                buf = "";
                for(int j =1;j<=table.getMetaData().getColumnCount();j++){
                    buf+=table.getString(j);
                }
                data.add(buf);
            }
            if(table!=null){table.close();}
            return data;
        }catch (Exception e){
            return null;
        }
    }*/
