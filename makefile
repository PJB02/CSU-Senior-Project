MAIN      = src/encryptor.cpp
EXEC      = encryptor
SRCS      = $(filter-out $(MAIN),$(wildcard src/*.cpp))
DOCTEST   = ../doctest/ # Shared between projects

# For warning options, see https://gcc.gnu.org/onlinedocs/gcc/Warning-Options.html
CPPFLAGS  = -std=c++20 -Wall -Wextra -Wconversion -Wshadow -Wpedantic -I src/

# Flags for debugging and catching problems. Removed in production for performance.
CPPFLAGS += -g -fsanitize=return -fsanitize=undefined -fsanitize=address

.PHONY: all run clean test-runner

# By default, make will compile the main executable.
all: $(EXEC)

# Compile and run the tests in a file by specifying the file name
# For example: make simple-test
%test:
	g++ $(CPPFLAGS) -I $(DOCTEST) test/$@.cpp $(SRCS) -o test-runner
	./test-runner

# Create the main executable
$(EXEC): $(SRCS) $(MAIN)
	g++ $(CPPFLAGS) $^ -o $@

# Run the main executable
run: $(EXEC)
	./$(EXEC)

# Clean up temporary files. Run this before committing.
clean:
	rm -f $(EXEC) test-runner
