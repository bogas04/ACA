#include <iostream>
#include<vector>
#include<list>
#include<cmath>
#include<set>
#include<algorithm>
using namespace std;

struct greedy{
    int from;
    int to;
    int cycles;
    bool visited;
};
list<greedy> d;

void modify (list<greedy>::iterator it2,bool new_visited) {
    greedy new_object;
    new_object.from = it2->from;
    new_object.to = it2->to;
    new_object.cycles = it2->cycles;
    new_object.visited = new_visited;
    d.erase(it2);
    d.push_back(new_object);
}
void modify (list<greedy>::iterator it2,int new_to, int new_cycles) {
    greedy new_object;
    new_object.from = it2->from;
    new_object.to = new_to;
    new_object.cycles = new_cycles;
    new_object.visited = false;
    d.erase(it2);
    d.push_back(new_object);
}
void print(greedy to_print){
    //cout << to_print.from <<endl << to_print.to << endl << to_print.cycles << endl;
    return;
}
bool operator<(const greedy& a, const greedy& b) {
    return (a.from < b.from);
}

list<greedy>::iterator find_state (int state_vector) {
    for(list<greedy>::iterator it = d.begin(); it != d.end(); it++) {
        if(it->from == state_vector) {
            return it;
        }
    }
    return d.end();
}

list<greedy>::iterator firstNonVisited(){
    cout << "Non visited called by d" << endl;
    for(list<greedy>::iterator it = d.begin();it!=d.end();it++){
        if(it->visited == false){
                cout << it->from << endl;
            return it;
        }
    }
    return d.end();
}
int main() {
	bool isEven = true;
	int n=3,t=7;
	int arr[][100]={{0,0,1,0,1,0,0},
                 {0,1,0,0,1,0,1},
                 {1,0,0,0,0,1,0},
                };


	vector<int> v;
	set<int> s;
	////cout << "Enter the number of stages and time required for one instruction to be executed";
	//cin >> n >> t;
	for(int i=0;i<n; i++){
		for(int j=0;j<t;j++){
			//cin >> arr[i][j];
			if(arr[i][j]==1){
				v.push_back(j);
				////cout << j <<endl;
			}
		}
		for(int a = 0;a<v.size();a++){
			for(int b=0;b<v.size();b++){
				if(a!=b){
                    ////cout << a << b << abs((v[a]-v[b])) << endl;
					s.insert(abs(v[a]-v[b]));
				}
			}
		}
			v.clear();
	}

	int initialStateVector = 0;
	for(set<int>::iterator it = s.begin(); it!=s.end(); it++){
		initialStateVector += pow(2,*it-1);
	}

	//cout << initialStateVector;
	greedy initial = {initialStateVector, initialStateVector,t,false};

	//print(initial);
	d.push_back(initial);


    list<greedy>::iterator parent_state = d.end();
    // for each state
	while((parent_state=firstNonVisited())!=d.end()){
        //parent_state->visited = true;
        modify(parent_state,true);
        int current_state = parent_state->from;
        //cout << "current_state in the set " << current_state << endl;

        // for each cycle in this state
        for(int cyclesComputed = 1; cyclesComputed <= t; cyclesComputed++) {
            if(current_state%2==0)
                isEven = true;
            else isEven = false;
            //  shift right

            current_state = current_state>>1;
            // create new state
            if(isEven){
                greedy l={current_state|initialStateVector, initialStateVector, t,false};

                int found = 0;
                for(list<greedy>::iterator it = d.begin();it!=d.end();it++){
                    if(it->from == l.from){
                        found=1;
                        break;
                    }
                }
                if(found==0)
                    d.push_back(l);
                // save
                if(parent_state->cycles > cyclesComputed) {
                    modify(parent_state,(current_state|initialStateVector),cyclesComputed);
                }
            }
        }
	}
	for(list<greedy>::iterator it = d.begin();it!=d.end();it++){
        cout << it->from << " -> " << it->to << " in cycles " << it->cycles<< " which is " << it->visited<<endl;
    }
    // for each state
	vector <double> avg_latencies;
	int g=0;
	list<greedy>::iterator current_state;
	for(list<greedy>::iterator it = d.begin();it!=d.end();it++){
        current_state = it;
        // self loop
        if(current_state->to == it->from) {
                cout << current_state->from << " [ " <<current_state->cycles << " ] -> ";
            cout << it->from<<endl;

            avg_latencies.push_back(it->cycles);
        } else {
            int cycles_traversed = 0;
            int states_traversed = 0;
            while(current_state->to != it->from) {
                cout << current_state->from << " [ " <<current_state->cycles << " ] -> ";
                cycles_traversed += current_state->cycles;
                current_state = find_state(current_state->to);
                if(current_state == d.end()) { //cout << "Unexpected error" ; return 0;
                }
                states_traversed++;
            }
            states_traversed++;
            cycles_traversed += current_state->cycles;
            cout << current_state->from << " [ " <<current_state->cycles << " ] -> ";
            cout << it->from<<endl;
            //cout << (cycles_traversed/states_traversed);
            cout <<  " AL : " << cycles_traversed/((1.0)*states_traversed) << endl;
            avg_latencies.push_back(cycles_traversed/((1.0)*states_traversed));

        }
	}

	cout << endl;
	sort(avg_latencies.begin(),avg_latencies.end());
    cout << avg_latencies[0] << endl;
	return 0;
}
