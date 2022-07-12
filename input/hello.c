#include <stdio.h>
#include <stdlib.h>

int main(int argc, char **argv) {
  printf("Hello, World!");
  
  if (argc == 2) {
  	printf(" > echo [%s]", argv[1]);
  }   
   printf("\n");
   exit(EXIT_SUCCESS);
}