import result.Feature;
import result.Geometry;
import result.Properties;
import result.Result;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DBHandler {
    private static final String url = "jdbc:h2:~/db";

    static void prepareDb() throws Exception {
        String userHomeDir = System.getProperty("user.home");
        if (new File(userHomeDir + "/db.mv.db").exists()) {
            System.out.println("db already exists");
            return;
        }

        final String sql = "CREATE TABLE LISTINGS AS SELECT * FROM CSVREAD('classpath:DB/listing-details.csv');";
        try (var con = DriverManager.getConnection(url)) {
            var stm = con.createStatement();
            final boolean execute = stm.execute(sql);
            System.out.println("DB created");
        } catch (SQLException ex) {
            throw new Exception("failed to create DB", ex);
        }

    }

    static Result readFromDb(Long minPrice, Long maxPrice, Long minBed, Long maxBed, Long minBath, Long maxBath) throws Exception {

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT id,street,status,price,bedrooms,bathrooms,sq_ft,lat,lng FROM LISTINGS WHERE 1=1");

        Connection con = null;
        ResultSet rs = null;

        List<Object> params = buildParams(minPrice, maxPrice, minBed, maxBed, minBath, maxBath, queryBuilder);

        try {
            con = DriverManager.getConnection(url);
            PreparedStatement preparedStatement = con.prepareStatement(queryBuilder.toString());

            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setLong(i+1, ((Long) params.get(i)).longValue());
            }

            rs = preparedStatement.executeQuery();
            Result result = new Result();
            result.type = "FeatureCollection";
            while (rs.next()) {
                Feature feature = new Feature();
                Properties properties = new Properties();
                properties.price = rs.getInt("price");
                properties.id = rs.getString("id");
                properties.bathrooms = rs.getInt("bathrooms");
                properties.bedrooms = rs.getInt("bedrooms");
                properties.sq_ft = rs.getInt("sq_ft");
                properties.street = rs.getString("street");
                feature.properties = properties;
                Geometry geometry = new Geometry();
                geometry.coordinates = Arrays.asList(rs.getDouble("lat"),rs.getDouble("lng"));
                feature.geometry = geometry;

                result.features.add(feature);
            }
            return result;
        } catch (Exception ex) {
            throw new Exception("failed to read from DB", ex);
        } finally {
            if (con != null) {
                con.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }

    private static List<Object> buildParams(Long minPrice, Long maxPrice, Long minBed, Long maxBed, Long minBath, Long maxBath, StringBuilder queryBuilder) {
        List<Object> params = new ArrayList<>();
        if (minPrice != null) {
            queryBuilder.append(" AND price >= ?");
            params.add(minPrice);
        }
        if (maxPrice != null) {
            queryBuilder.append(" AND price <= ?");
            params.add(maxPrice);
        }
        if (minBed != null) {
            queryBuilder.append(" AND bedrooms >= ?");
            params.add(minBed);
        }
        if (maxBed != null) {
            queryBuilder.append(" AND bedrooms <= ?");
            params.add(maxBed);
        }
        if (minBath != null) {
            queryBuilder.append(" AND bathrooms >= ?");
            params.add(minBath);
        }
        if (maxBath != null) {
            queryBuilder.append(" AND bathrooms <= ?");
            params.add(maxBath);
        }
        return params;
    }
}
