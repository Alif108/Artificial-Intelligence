import numpy as np
import matplotlib.pyplot as plt

# function to search maxima
def h(x):
    if x<-1 or x>1:
        y = 0;
    else:
        y = np.cos(50*x) + np.sin(20*x)
    return y

hv = np.vectorize(h)


# Hill climbing 
def hill_climbing(func, start=0, N=100):
    # params: func -> function to climb
    # start -> starting position
    # N -> number of steps
    
    x = start
    history = []
    
    for i in range(N):
        history.append(x)   # keep track of steps
        
        u = 0.001
        xleft, xright = x-u, x+u  # small step size
        yleft, yright = func(xleft), func(xright)  # getting the value along the given function
        
        if yleft > yright:
            x = xleft
        else:
            x = xright
    
    return x, history  # maxima, path to maxima



def simulated_annealing(search_space, func, T):
	# params: search_space -> numpy array within a range
	# func -> function to search
	# T -> starting temperature

    scale = np.sqrt(T)
    start = np.random.choice(search_space)   # randomly choosing a point to start within search spcae
    x = start * 1
    cur = func(x)
    history = [x]
    for i in range(1000):
        prop = x + np.random.normal()*scale
        
        # if proposed point doesn't lie in search space
        # or if E/T less than threshold
        if prop > search_space[len(search_space)-1] or prop < search_space[0] or np.log(np.random.rand())*T > (func(prop) - cur):
            prop = x
        
        x = prop
        cur = func(x)
        T = 0.9 * T 		# decreasing the temperature 
        history.append(x)
        
    return x, history



# ------------------------------------------- Driver Code ------------------------------------------ #

# X = np.linspace(-1, 2, num=1000)
# plt.plot(X, hv(X))

# x0, history = hill_climbing(hv, 0.6, 100)
# plt.plot(X, hv(X))                           # plot the original graph
# plt.scatter(x0, h(x0), marker='x', s=100)    # mark the maxima
# plt.plot(history, hv(history))               # mark the path to maxima


x = np.linspace(-1, 1, num=1000)
x1, history = simulated_annealing(x, h, T=4)

plt.plot(X, hv(X))
plt.scatter(x1, hv(x1), marker = 'x')
plt.plot(history, hv(history))