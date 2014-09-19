Specs:
Ram: 16 GB
Processor: Intel i7 @3.6 GHz

Assignment 4.1

1:
See \pcpp-week04\Outputs\Exercise_4.1.1.txt

	Mark 1: 
	No suprise. Should the count increase to let's say 
	100 million, then the time will drop to ~0 ns. This is because
	of the JIT compiler that will actually skip the calculation
	as this is never used.

	Mark 2: 
	The numbers are pretty much equal to those in the text
	The JIT cannot make the clever optimization it did last time, 
	which causes the time to increase.

	Mark 3: 
	All looks good here. All we did was running multiple tests.
	The times are somewhat consistant with Mark 2, as expected.

	Mark 4: 
	We added the standard deviation. Again, same results.
	The plus-minus indicates the lower and upper bound.

	Mark 5:
	Here we start doubling count till time spent >= 0.25 secs.
	We can immediately observe strange behavior at 1430.2ns.
	It is highly likely that one of the system processes used
	some of the available cpu power here.

	Mark 6:
	Here it is possible to provide an object of a class
	that implements IntToDouble. As seens on the std.
	deviation there is nothing out of the ordinary here.

2:
See \pcpp-week04\Outputs\Exercise_4.1.2.txt

	We notice that the function which uses more ns
	typically has a higher deviation. This is however expected
	(due to the nature of how it is calculated).

	We can also observe more "irregularities". This is due to
	the functions taking a lot more time than multiply. As in
	there is a larger time span where the process can be "interrupted".
	This can be seen POW with 15 and 14 deviations.


Assignemnt 4.2

1:
	hasCode()
	sdev is spiking a single time because of other CPU tasks.
	Apart from this both numbers seems to be decreasing as they should

	Point creation
	Here it is clear to see that declaring objects requires a lot more time
	than simply executing a method.
	The mean and sdev seems to be constantly spiking for the fist 6 objects,
	and afterwards starting to decrease somewhat steady.

	Thread create
	We see here that it is very expensive to create threads, thus it does not
	seems worth it to create threads for smaller computations.
	Like with point creation, we also observe a rather unsteady sdev that seems
	to be going up and down.

	Thread create start
	There were little to no suspecious about starting the threads
	except for the actual time it took to start one, which was like 100
	times larger than creating it.

	Thread create start join
	The join time was really large. One must however keep in mind
	that this is because the actual computation, starting the thread,
	and even creating the thread is a part of the reported results here

	Thread's work
	The actual work of the thread (for loops and getAndIncrement).
	We observe a few spikes which are likely due to the OS and other processes.

	Uncontended lock
	Usually 5 ns which is the time it takes to lock. 
	The sdev and time is nearly constant.

2:
	hashCode()
	A sdev of 0 seems a bit wierd. And suggests that the hashCode
	takes constant 2.4 ns

	Point creation
	Not surprisingly it takes longer time to create the objects 

	Thread create
	We see that this takes longer than creating a point.
	We can conclude that a thread object must be somewhat
	more complicated than a point.

	Thread create start
	We can conclude that it takes a long time to actually start
	the thread rather than simply creating it.

	Thread create start join
	This part takes the longest. The reason for this, is as described earlier
	that this include creating the thread, starting it, doing the actual work,
	and waiting for it to finish (.join()). 

	Thread's work
	The actual work of the thread. Obviously this takes quite a while,
	which leads to a higher sdev compared to calling the multiply method 
	or creating a point.

	Uncontended lock:
	After observing that taking a free lock usually took 5.0 ns
	the result 5.5 ns might be surpricing. One must keep in mind 
	that it takes longer with a lower count, and this is likely the reason
	that the number is not 5.0 ns.


Assignment 4.3

1:
See \pcpp-week04\Outputs\Exercise_4.3.1.txt 

2: 
See \pcpp-week04\Outputs\Exercise_4.3.2.JPG

3: 
Everything with the exception of perhaps number 5 looks fine.
At 5 a spike occurs, probably because of something else stealing resources.
We see that the curve decreases a lot in the first couple of threads,
until one point where the creation of threads makes the program slower
because it cannot run all the (expensive) threads that were created. 

4:
See \pcpp-week04\Outputs\Exercise_4.3.4.txt 

	The stability does not seems to have increased compared to the
	LongCounter, in fact the number of "spikes" are the same.
	We can also observe that the pattern behave (as expected) the same.
	The time increases until such point where it is no longer worth
	creating new threads.
	This occurs at the exact same spot (10) as using the LongCounter.
	We can also observe a general decrease in time spent.

	Generally built-in classes are optimized and efficient. If there 
	are no special requirements, one should use the in-built ones.
	There is no need to reinvent the wheel - if it's not a special type
	of wheel.

5:
See \pcpp-week04\Outputs\Exercise_4.3.5.txt 

	The running time is roughly the same.
	A couple of qualified guesses to why this happens could be
	1) the JIT compiler performs some clever optimizations
	2) The AtomicLong is built for this? - We have not tested this
	on the LongCounter.