package com.CaSPR.mcdense;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


/**
 * Md Mizanur Rahman
 * PhD CS, FIU
 */


public class MCD {
	 Graphv2 originalGraph; 
    private  final double delta_filter = 0.4;
    private  final double rho_filter = 0.4;
	private  int iter = 100;
	private  RandomString rndStr;
	 int totalComp = 0;
	private  List<String> allCompUserIds;
	private  List<List<String>> mcdComponents; 
	
	public MCD(Graphv2 graph) {
		originalGraph = graph;
		rndStr = new RandomString(10);
		allCompUserIds = new ArrayList<String>();
		mcdComponents = new ArrayList<List<String>>();
	}

	public  Graphv2 minCut( Graphv2 gr ) {
        Random rnd = new Random();
        while( gr.vertices.size() > 2) {
            if (gr.edgev2s.size() == 0 || gr.edgev2s.size() == 1) break; 
            Edgev2 edgev2 = gr.edgev2s.remove( rnd.nextInt( gr.edgev2s.size() ) );
            
            Vertexv2 v1 = cleanVertex( gr, edgev2.ends.get( 0 ), edgev2 );
            Vertexv2 v2 = cleanVertex( gr, edgev2.ends.get( 1 ), edgev2 );
            //contract
            Vertexv2 mergedVertex = new Vertexv2(v1,v2);
            redirectEdges( gr, v1, mergedVertex );
            redirectEdges( gr, v2, mergedVertex );
            
            gr.addVertex( mergedVertex );
        }
        return gr;
    }
	
	@SuppressWarnings("unused")
	private  int getMinCutValue(List<String> userids) { 
		int min = 100000;
        Graphv2 finalMinCutGraph = null; 
        for( int i = 0; i < 25; i++ ) {
            Graphv2 gr = Utilityv2.createGraphFromGraph(userids,originalGraph);
            
            Graphv2 minCutGraph = minCut( gr ); 
            int currMin = minCutGraph.edgev2s.size();
            min = Math.min( min, currMin );
            if (min == currMin) {
            	finalMinCutGraph = minCutGraph;
			}
        }
        
        return finalMinCutGraph.edgev2s.size();   
	}
	
    private  Vertexv2 cleanVertex( Graphv2 gr, Vertexv2 v, Edgev2 e ) {
        gr.vertices.remove( v.lbl );
        v.edgev2s.remove( e );
        return v;
    }
    
    private  void redirectEdges( Graphv2 gr, Vertexv2 fromV, Vertexv2 toV ) {
        for ( Iterator<Edgev2> it = fromV.edgev2s.iterator(); it.hasNext(); ) {
            Edgev2 edgev2 = it.next();
            it.remove();
            if( edgev2.getOppositeVertex( fromV ) == toV ) {
                //remove self-loop
                toV.edgev2s.remove( edgev2 );
                gr.edgev2s.remove( edgev2 );
            } else {
                edgev2.replaceVertex( fromV, toV );
                toV.addEdge( edgev2 );
            }
        }
    }
    
	public void startMCD(String pid, List<String> userids) {
		String ownid = rndStr.nextString();
		if (userids.size() <= 5) return;
		int min = 100000;
        Graphv2 finalMinCutGraph = null;
        float maxAvgDelta = 0;
        //int min_cut_value = getMinCutValue(userids);
        float init_delta = getDelta(userids);
        double init_rho = getRho(userids);
        
        if (init_delta > delta_filter && userids.size() >= 5){
        	handleCompUsers(userids);
        	return;
        }
        
        for( int i = 0; i < iter; i++ ) {
            Graphv2 gr = Utilityv2.createGraphFromGraph(userids,originalGraph); 
            Graphv2 minCutGraph = minCut( gr );
            
            if (minCutGraph.vertices.size() == 2) {
            	float totalDelta = 0;
            	
            	for (String key : minCutGraph.vertices.keySet()) {
                	Vertexv2 vertexv2 = minCutGraph.vertices.get(key);
                	
                	float delta  = 0;
                	if (vertexv2.isCom){
                		delta = getDelta(vertexv2.userids);
                		
                		if (vertexv2.userids.size() < 5) {
                			delta = 0;
						}
                		
                		//System.out.println("this graph delta " + delta); 
                	}
                	
                	totalDelta += delta;
                }
            	
            	float currentAvgDelta = totalDelta/2;
            	
            	//System.out.println("avg delta " + currentAvgDelta + "\n"); 
            	//boolean isFound = false;
            	
            	if (currentAvgDelta >= maxAvgDelta) {
            	//if (currentAvgDelta > maxAvgDelta  && minCutGraph.edges.size() < 3*min_cut_value) {
            		maxAvgDelta = currentAvgDelta;
            		finalMinCutGraph = minCutGraph;
            		//isFound = true;
				}

//            	if(!isFound && finalMinCutGraph == null){
//            		if (currentAvgDelta > maxAvgDelta){
//            			maxAvgDelta = currentAvgDelta;
//                		finalMinCutGraph = minCutGraph;
//            		}
//            	}
            	
            	int currMin = minCutGraph.edgev2s.size();
	            min = Math.min( min, currMin );
			}else{
				int currMin = minCutGraph.edgev2s.size();
	            
	            min = Math.min( min, currMin );
	            if (min == currMin) {
	            	finalMinCutGraph = minCutGraph;
				}
			}
        }
        
        //stopping
        for (String key : finalMinCutGraph.vertices.keySet()) {
        	Vertexv2 vertexv2 = finalMinCutGraph.vertices.get(key);
        	if (vertexv2.isCom) {
        		float delta = getDelta(vertexv2.userids);
        		double rho = getRho(vertexv2.userids);
        		//if (delta > delta_filter && vertexv2.userids.size() >= 5) {
        		if (rho > init_rho && rho > rho_filter && vertexv2.userids.size() >= 5) {
        			//System.out.println("delta " + delta); 
        			handleCompUsers(vertexv2.userids);
        			totalComp++;
				}else{
					startMCD(ownid,vertexv2.userids); 
				}
			}
		}
	}

	private  void handleCompUsers(List<String> userids) {
	 	for (String userid : userids){ 
	 		//System.out.println(userid + "," + "#2");
	 		if(!allCompUserIds.contains(userid)){
	 			allCompUserIds.add(userid);
	 		}
	 	}
	 	
	 	mcdComponents.add(userids);
	}

	private  float getDelta(List<String> userids) {
		Graphv2 gr = Utilityv2.createGraphFromGraph(userids,originalGraph); 
		return gr.getDelta();
	}
	
	private  double getRho(List<String> userids) {
		Graphv2 gr = Utilityv2.createGraphFromGraph(userids,originalGraph); 
		return gr.getRho();
	}

	public List<List<String>> getMCDComponents() { 
		return mcdComponents;
		
	}
 
	public List<String> getAllCompUserIds() {
		return allCompUserIds;
		
	}

	//+++++++++++++++++++++Best Combination++++++++++ 0.4, 10
	//+++++++++++++++++++++Best Combination++++++++++ 0.5, 15
}
