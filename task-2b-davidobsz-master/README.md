# Matrix Exercise

The matrix task has a number of goals:
- Learn to add a module to a project
- Learn to add a module dependency to a module
- Learn to use the debugger
- Getting familiar with the `Matrix` classes that will also be used in the assignment

## Introduction
This exercise contains in `sudokuLib` a class `SudokuGrid` that models some of the rules of Sudoku. It also has a test
suite (use the "Test all" configuration) that you can use to run the tests (all 1625 of them).

## The Matrix "library"
The matrix classes are provided in the `lib` folder. There are a number of key interfaces and classes in this library.
It uses operator overloading to make the classes feel like they are regular 2d arrays.

- `interface Matrix<T>` - This is a readOnly view of a fixed size 2-dimensional "matrix" or array of values. 
- `interface MutableMatrix<T>` - This is an updatable version of `Matrix` whose values can be changed
- `interface SparseMatrix<T>` - This is not needed in this exercise, but a sparse matrix allows "holes" to exist in the
              matrix.
- `interface MutableSparseMatrix<T>` - Mutable/changeable version of `SparseMatrix`


- `class ArrayMutableMatrix<T>` - This is an implementation of `MutableMatrix<T>` backed by an `Array`
- `class ArrayMutableSparseMatrix<T>` - This is an implementation of `MutableSparseMatrix<T>` backed by an `Array` and a
              function that is used to determine whether the particular coordinates are used.
- `class Coordinate<T>` - This is a class that represents a point in a `Matrix<T>`. The type parameter is merely used
              to keep different coordinates separate for the compiler, but not used any further.
              
## Steps
1. Try to run all tests. You will find that this will not work, because the Matrix classes are not present.
2. You can see that there is a `lib` folder, but it is not a module (look at `sudokuLib` and you see that folder being
   decorated).

   You will need to add the `lib` folder to the project. To do this, edit `settings.gradle` and add `include("lib")` as 
   a new line in this file.
3. Try running the test again. You will find that it still doesn't work, this is because sudokuLib needs to be
   configured to use the `lib` module.
   
   You need to add a dependency to `sudokuLib/build.gradle`. You will find a `dependencies` section in this file. Add a
   compile time dependency by adding the line `implementation project(":lib")` to the project
   
4. Now the project should compile. You can run the "Test all" configuration that has already been created. You will
   find that quite some of the tests fail. Don't worry, there are only 3 small bugs hidden in the code of `SudokuGrid`.
   
   To solve these bugs, you want to run the test individually (makes debugging easy), use breakpoints, step into, step
   over, etc.. After each fix you should find that the overall amount of bugs is lower.
   
   While you may be able to solve the issues by just looking at the code, this task is intended to help you practice
   using the debugger, so do so.
   
