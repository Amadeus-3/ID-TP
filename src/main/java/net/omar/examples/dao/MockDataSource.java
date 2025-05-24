package net.omar.examples.dao;

public class MockDataSource {
    private String url;
    private String username;
    private int maxConnections;
    
    public MockDataSource() {
        System.out.println("MockDataSource created");
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
        System.out.println("Setting URL: " + url);
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
        System.out.println("Setting username: " + username);
    }
    
    public int getMaxConnections() {
        return maxConnections;
    }
    
    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
        System.out.println("Setting maxConnections: " + maxConnections);
    }
    
    @Override
    public String toString() {
        return "MockDataSource{" +
                "url='" + url + '\'' +
                ", username='" + username + '\'' +
                ", maxConnections=" + maxConnections +
                '}';
    }
}