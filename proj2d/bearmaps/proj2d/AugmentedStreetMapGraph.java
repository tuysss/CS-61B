package bearmaps.proj2d;

import bearmaps.lab9.MyTrieSet;
import bearmaps.proj2ab.Point;
import bearmaps.proj2c.streetmap.StreetMapGraph;
import bearmaps.proj2c.streetmap.Node;
import bearmaps.proj2ab.KDTree;

import java.util.*;

/**
 * An augmented graph that is more powerful than a standard StreetMapGraph.
 * Specifically, it supports the following additional operations:
 *
 *
 * @author Alan Yao, Josh Hug, ________
 */
public class AugmentedStreetMapGraph extends StreetMapGraph {
    private Map<Point,Long> pointToID;
    private KDTree kdTree ;

    private MyTrieSet trieSet;
    private Map<String,List<Node>> cleanedNameToNodes;

    /**
     * build 2 trees in this constructor:
     * TrieSet : to autocomplete with prefix
     * KDTree : to find the closest path
     */
    public AugmentedStreetMapGraph(String dbPath) {
        super(dbPath);
        // You might find it helpful to uncomment the line below:
        //List<Node> nodes = this.getNodes();
        pointToID=new HashMap<>();
        List<Point> points=new LinkedList<>();
        List<Node> nodes=this.getNodes();

        trieSet=new MyTrieSet();
        cleanedNameToNodes=new HashMap<>();
        List<Node> nodesList;

        for(Node node:nodes){
            //if the node has a name, clean it, then add it to the tire tree
            //and put the <cleaned name, list of nodes> pair into hashmap
            if(node.name()!=null){
                String cleaned=cleanString(node.name());
                trieSet.add(cleaned);

                if(!cleanedNameToNodes.containsKey(cleaned)){
                    cleanedNameToNodes.put(cleaned,new LinkedList<>());
                }
                nodesList=cleanedNameToNodes.get(cleaned);
                nodesList.add(node);
                cleanedNameToNodes.put(cleaned,nodesList);
            }

            //only consider the nodes that have neighbors(which is a road ,not a place)
            //turn these <Node>s into <Point>s to service KDTree
            long id=node.id();
            if(!this.neighbors(id).isEmpty()){
                double x=node.lon();
                double y=node.lat();
                Point point=new Point(x,y);

                points.add(point);
                pointToID.put(point,id);
            }
        }
        kdTree=new KDTree(points);

    }


    /**
     * For Project Part II
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    public long closest(double lon, double lat) {
        Point closest= kdTree.nearest(lon,lat);
        return pointToID.get(closest);
    }


    /**
     * For Project Part III (gold points)
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     * @param prefix Prefix string to be searched for. Could be any case, with or without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        String cleanedPrefix=cleanString(prefix);
        List<String> matching=trieSet.keysWithPrefix(cleanedPrefix);
        Set<String> locations=new HashSet<>();

        for(String name: matching){
            for(Node node: cleanedNameToNodes.get(name)){
                locations.add(node.name());
            }
        }

        return new LinkedList<>(locations);
    }

    /**
     * For Project Part III (gold points)
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose cleaned name matches the
     * cleaned <code>locationName</code>, and each location is a map of parameters for the Json
     * response as specified: <br>
     * "lat" -> Number, The latitude of the node. <br>
     * "lon" -> Number, The longitude of the node. <br>
     * "name" -> String, The actual name of the node. <br>
     * "id" -> Number, The id of the node. <br>
     */
    public List<Map<String, Object>> getLocations(String locationName) {
        List<Map<String, Object>> locations=new LinkedList<>();
        String cleaned=cleanString(locationName);

        if(cleanedNameToNodes.containsKey(cleaned)){
            for(Node node: cleanedNameToNodes.get(cleaned)){
                Map<String,Object> locInfo=new HashMap<>();
                locInfo.put("lat",node.lat());
                locInfo.put("lon",node.lon());
                locInfo.put("name",node.name());
                locInfo.put("id",node.id());
                locations.add(locInfo);
            }
        }

        return locations;
    }


    /**
     * Useful for Part III. Do not modify.
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

}
