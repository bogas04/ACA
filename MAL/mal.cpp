#include <iostream>
#include<vector>
#include<set>
#include<cmath>

using namespace std;
struct greedy{
    int from;
    int to;
    int cycles;
};
bool greedy_compare(const greedy *a, const greedy *b) {
    return (a->from - b->from);
}
greedy* find_state (set<greedy, greedy_compare> d,int state_vector) {
    for(iterator<greedy>::it = d.begin(); it != d.end(); it++) {
        if(*it.from == state_vector) {
            return it;
        }
    }
    return NULL;
}
int main() {
	int n,t;
	int arr[100][100];
	vector<int> v;
	set<int> s;
	cout << "Enter the number of stages and time required for one instruction to be executed";
	cin >> n >> t;
	for(int i=0;i<n; i++){
		for(int j=0;j<t;j++){
			cin >> arr[i][j];
			if(arr[i][j]==1){
				v.push_back(j);
			}
		}
		for(int a = 0;a<v.size();a++){
			for(int b=0;b<v.size();b++){
				if(a!=b){
					cout << abs(v[a]-v[b])<<endl;
					s.insert(abs(v[a]-v[b]));
				}
			}
		}
			v.clear();
	}

	int initialStateVector = 0;
	for(set<int>::iterator it = s.begin();it!=s.end();it++){
		cout << *it;
		initalStateVector += pow(2,*it-1);
	}

	cout << initalStateVector;
	greedy initial = {initialStateVector, initalStateVector,t};

	set<greedy> d(&greedy_compare>);
	d.insert(initial);

    // for each state
	for(set<greedy>::iterator it = d.begin();it!=d.end();it++){
        int current_state = it->from;

        // for each cycle in this state
        for(int cyclesComputed = 1; cyclesComputed <= t; cyclesComputed++) {
            //  shift right
            current_state= current_state>>1;

            // create new state
            d.insert({current_state|initialStateVector, initialStateVector, t});

            // save
            if(it->cycles > cyclesComputed) {
                it->to = current_state|initialStateVector;
                it->cycles = cyclesComputed;
            }
        }
	}

	// for each state
	vector <int> avg_latencies[t];
	for(set<greedy>::iterator it = d.begin();it!=d.end();it++){
        greedy current_state = *it;

        // self loop
        if(current_state.to == it->from) {
            avg_latencies.push_back(*it.cycles);
        } else {
            int cycles_traversed = 0;
            int states_traversed = 0;
            while(current_state.to != *it.from) {
                cycles_traversed += current_state.cycles;
                current_state = find_state(d, current_state.to);
                if(current_state == NULL) { cout << "Unexpected error" ; return 0;}
                states_traversed++;
            }
            avg_latencies.push_back(cycles_traversed/states_traversed);
        }
	}

	return 0;
}
