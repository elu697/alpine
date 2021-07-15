#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <signal.h>
#include <ctype.h>
#include <limits.h>
#include <sys/time.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <sys/types.h>
#include <ifaddrs.h>
#include <sys/ioctl.h>
#include <net/if.h>

#include <cefore/cef_define.h>
#include <cefore/cef_frame.h>
#include <cefore/cef_client.h>
#include <cefore/cef_log.h>

int main()
{
    //gcc test.c -lcefore -lssl -lcrypto -o main
    fprintf(stdout, "test.c start\n");

    int res;
    cef_log_init("cefpyco", 1);
    cef_frame_init();
    res = cef_client_init(9896, "/usr/local/cefore");
    fprintf(stdout, "res %d\n", res);
    fprintf(stderr, "errot test\n");

    return 0;
}
