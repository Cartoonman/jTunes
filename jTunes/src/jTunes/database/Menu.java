package jTunes.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;

public class Menu {
    public enum ValueType {
        genre, artist, album, song
    }
    
    private String query;
    private Connection connection;
    private Statement statement;
    private PreparedStatement pStatement;
    private ResultSet resultSet;
    private AudioPlayer audioPlayer = new AudioPlayer();
    
    private int genreID;                        // require for a compound query
    
    private static final Map<String, String> QUERIES;
    static {
        Map<String, String> aMap = new HashMap<String, String>();
        // basic queries
        aMap.put("genres",  "SELECT genreName FROM Genre;");
        aMap.put("artists", "SELECT artistName FROM Artist;");
        aMap.put("albums",  "SELECT albumName FROM Album;");
        aMap.put("songs",   "SELECT songName FROM Songs;");
        QUERIES = Collections.unmodifiableMap(aMap);
    }
    
    public Menu() throws SQLException {
        try {
            connection = DatabaseSetup.getConnection();
        } catch (MySQLSyntaxErrorException e) {
            System.out.println("ERROR IN SQL DB, DATABASE CORRUPT");
            e.printStackTrace();
        }
    }
    
    public List<String> getValues(ValueType type) {
        List<String> list = new ArrayList<>(5);
        
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(
                    QUERIES.get(type.name() + "s"));
            
            while (resultSet.next()) {
                String column = type.name() + "Name";
                list.add(resultSet.getString(column));
                System.out.println(resultSet.getString(column));
            }
        } catch (SQLException e) {
            System.out.println("Error occured when reading database.");
            e.printStackTrace();
        } finally {
            try { resultSet.close(); } catch (Exception e) {}
            try { statement.close(); } catch (Exception e) {}
        }
        
        return list;
    }
    
    public List<String> getArtistsInGenre(String genreName) {
        List<String> list = new ArrayList<>(5);
        
        try {
            query = "SELECT artistName FROM Artist "
                  + "WHERE genreID = (SELECT genreID FROM Genre "
                                   + "WHERE genreName = ?);";
            pStatement = connection.prepareStatement(query);
            pStatement.setString(1, genreName);
            resultSet = pStatement.executeQuery();
            
            while (resultSet.next()) {
                list.add(resultSet.getString("artistName"));
                System.out.println(resultSet.getString("artistName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { resultSet.close(); } catch (Exception e) {}
            try { pStatement.close(); } catch (Exception e) {}
        }
        
        return list;
    }
    public List<String> getAlbumsByArtistInGenre(String artistName, String genreName) {
    	List<String> list = new ArrayList<>(5);
        try {
            query = "SElECT albumName FROM Album "
                  + "WHERE artistID = (SELECT artistID FROM Artist "
                                    + "WHERE  artistName = ?) "
                  + "  AND  genreID = (SELECT genreID FROM Genre "
                                    + "WHERE  genreName = ?);";
            pStatement = connection.prepareStatement(query);
            pStatement.setString(1, artistName);
            pStatement.setString(2, genreName);
            resultSet = pStatement.executeQuery();
            
            while (resultSet.next()) {
            	list.add(resultSet.getString("albumName"));
                System.out.println(resultSet.getString("albumName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { resultSet.close(); } catch (Exception e) {}
            try { pStatement.close(); } catch (Exception e) {}
        }
        return list;
    }
    public List<String> getSongsInAlbumByArtistInGenre(String albumName) {
    	List<String> list = new ArrayList<>(5);
        try {
            query = "SELECT songTitle FROM Songs "
                  + "WHERE  albumID = (SELECT albumID FROM Album "
                                    + "WHERE  albumName = ?);";
            pStatement = connection.prepareStatement(query);
            pStatement.setString(1, albumName);
            resultSet = pStatement.executeQuery();
            
            while (resultSet.next()) {
            	list.add(resultSet.getString("songTitle"));
                System.out.println(resultSet.getString("songTitle"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { resultSet.close(); } catch (Exception e) {}
            try { pStatement.close(); } catch (Exception e) {}
        }
        return list;
    }
    
    public void getArtists(String genreName) {
        try {
            query = "SELECT genreID FROM Genre WHERE genreName = '" + genreName + "';";
            resultSet = statement.executeQuery(query);
            resultSet.next();
            genreID = resultSet.getInt("genreID");
            printArtists(genreID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void printArtists(int genreID) {
        
        try {
            query = "SELECT artistName FROM Artist WHERE genreID = " + genreID + ";";
            resultSet = statement.executeQuery(query);
            while(resultSet.next()) {
                System.out.println(resultSet.getString("artistName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getAlbums(String artistName) {
        int artistID = 0;
        try {
            query = "SELECT artistID FROM Artist WHERE artistName = '" + artistName + "';";
            resultSet = statement.executeQuery(query);
            resultSet.next();
            artistID = resultSet.getInt("artistID");
            printAlbums(artistID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void printAlbums(int artistID) {
        try {
            query = "SELECT albumName FROM Album "
                    + "WHERE artistID = " + artistID 
                    + " AND genreID = " + genreID + ";";
            resultSet = statement.executeQuery(query);
            while(resultSet.next()) {
                System.out.println(resultSet.getString("albumName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }    
    }
    
    public void getSongs(String albumName) {
        int albumID = 0;
        try {
            query = "SELECT albumID FROM Album WHERE albumName = '" + albumName + "';";
            resultSet = statement.executeQuery(query);
            resultSet.next();
            albumID = resultSet.getInt("albumID");
            printSongs(albumID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void printSongs(int albumID) {
        try {
            query = "SELECT songTitle FROM Songs WHERE albumID = " + albumID + ";";
            resultSet = statement.executeQuery(query);
            while(resultSet.next()) {
                System.out.println(resultSet.getString("songTitle"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }    
    }
    
    public boolean activateSong(String song) {
        if(!audioPlayer.load(song)) return false;
        while(!audioPlayer.isCompleted()) {
            // System.out.println(audioPlayer.getSongProgress()); // not necessary but can be useful for GUI
            audioPlayer.menu();
        }
        return true;
    }
}
