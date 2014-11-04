Overview
========
This is getting a little complex, so let me explain. I used
the interfaces I was talking about last time to encapsulate packing
and labeling as two different problems.

Here's the advantage - it allows me to create cool things like
ChooseBestPackingStrategy, a packing strategy that takes in a list
of other strategies, runs them all, and then chooses the best, or
ProgressiveLabelingStrategy, which runs a series of labeling strategies
in order to result in a final labeling.

It also simplifies the code for each of these strategies, since
you only need to think about the small amount of code needed to do one thing,
and no need to navigate around huge documents.

To define/test a player, all you have to do is choose a packing strategy
and a labeling strategy. Then, pass those as arguments to Strategy().

Creating Strategies
===================
To create a packing strategy, just create a class in common/, and make
sure it implements IPackingStrategy, which just means that it has this
function in it:

    ArrayList<seed> generatePacking(ArrayList<Pair> trees, double width, double height);

To create a labeling strategy, just create a class in common/, and make
sure it implements ILabelingStrategy, which just means that it has this
function in it:

    void labelSeeds(ArrayList<seed> seeds, double s)

You can also create a constructor, but you don't need to. To test it out,
just use your class in Player's init() function instead of the ones I'm
currently using.
