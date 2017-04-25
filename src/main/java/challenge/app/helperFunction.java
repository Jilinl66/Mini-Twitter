/**
 * Compute shortest distance
 * 
 * @author Jilin Liu
 */
package challenge.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import challenge.object.Followers;

public class helperFunction {
	public int dist = 0;
	
	public int shorestDist(List<Followers> list, int p1, int p2) {
		Map<Integer, ArrayList<Integer>> map = new HashMap<>();
		for(Followers f: list){
			int person_id = f.getPerson_id();
			int follower_person_id = f.getFollower_person_id();
			if(map.get(follower_person_id) == null)
				map.put(follower_person_id, new ArrayList<>());
			map.get(follower_person_id).add(person_id);
		}
		dfs(map, p1, p2);
		return dist;
	}
	
	private void dfs(Map<Integer, ArrayList<Integer>> map, int p1, int p2) {
		boolean[] visited = new boolean[map.size() + 1];
		Queue<Integer> queue = new LinkedList<>();
		ArrayList<Integer> followed = map.get(p1);
		for(int i: followed){
			queue.offer(i);
			visited[i] = true;
		}
		dist ++;
		while(!queue.isEmpty()) {
			int size = queue.size();
			for(int i = 0; i < size; i++) {
				int top = queue.poll();
				if(top == p2)
					return;
				else{
					for(int j: map.get(top)){
						if(!visited[j]){
							queue.offer(j);
							visited[j] = true;
						}
					}
				}
			}
			dist++;
		}
	}
}
