import com.google.gson.Gson;
import result.Result;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class RealiServlet extends HttpServlet {

    public static final String MIN_PRICE = "min_price";
    public static final String MAX_PRICE = "max_price";
    public static final String MIN_BED = "min_bed";
    public static final String MAX_BED = "max_bed";
    public static final String MIN_BATH = "min_bath";
    public static final String MAX_BATH = "max_bath";


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        final Map<String, String[]> parameterMap = request.getParameterMap();
        Long minPrice = getParam(parameterMap, MIN_PRICE);
        Long maxPrice = getParam(parameterMap, MAX_PRICE);

        Long minBed = getParam(parameterMap, (MIN_BED));
        Long maxBed = getParam(parameterMap, (MAX_BED));

        Long minBath = getParam(parameterMap, (MIN_BATH));
        Long maxBath = getParam(parameterMap, (MAX_BATH));

        Result result = null;
        try {
            result = DBHandler.readFromDb(minPrice, maxPrice, minBed, maxBed, minBath, maxBath);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("failed to read from DB");
        }

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        String resultJson = new Gson().toJson(result);
        response.getWriter().println(resultJson);
    }

    private Long getParam(Map<String, String[]> parameterMap, String paramName) {
        return parameterMap.containsKey(paramName) ? Long.parseLong(parameterMap.get(paramName)[0]) : null;
    }

}