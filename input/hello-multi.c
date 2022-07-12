//https://dev-notes.eu/2019/07/Get-a-line-from-stdin-in-C/
#include <stdio.h>
#include <stdlib.h>

int main(int argc, char **argv)
{

  char *line = NULL;
  size_t len = 0;
  ssize_t read;

  if (argc == 2) {
  	printf("parameter [%s]\r\n", argv[1]);
  }  
  fflush(stdout);
  while ((read = getline(&line, &len, stdin)) != -1) {
  	printf("echo > %s", line);
  fflush(stdout);
  }
  
  free(line);

  exit(EXIT_SUCCESS);
}