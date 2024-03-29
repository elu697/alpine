#                 -*- Autoconf -*-
# Process this file with autoconf to produce a configure script.

dnl
dnl  project setting
dnl
AC_PREREQ([2.69])
AC_INIT([cefore], [0.8.3], [])
AC_CONFIG_AUX_DIR(autotools)
AC_CONFIG_SRCDIR([config.h.in])
AC_CONFIG_HEADERS([config.h])
AC_CONFIG_MACRO_DIR([m4])
# limit the output message
AM_SILENT_RULES([yes])
AM_INIT_AUTOMAKE([foreign])

# Checks for programs.
cflags_save="$CFLAGS"
CFLAGS="$cflags_save"
test -n "$CFLAGS" || CFLAGS=""
AC_PROG_CC
AM_PROG_CC_C_O
LT_INIT([dlopen])
# AC_PROG_RANLIB

# Checks for typedefs, structures, and compiler characteristics.
#AC_TYPE_INT64_T
#AC_TYPE_MODE_T
#AC_TYPE_PID_T
#AC_TYPE_SIZE_T
#AC_TYPE_UINT16_T
#AC_TYPE_UINT32_T
#AC_TYPE_UINT64_T
#AC_TYPE_UINT8_T

# Checks for library functions.
AC_FUNC_FORK
#AC_FUNC_LSTAT_FOLLOWS_SLASHED_SYMLINK
AC_FUNC_MALLOC
#AC_CHECK_FUNCS([clock_gettime gethostbyaddr gettimeofday inet_ntoa memset mkdir rmdir socket strcasecmp strchr strerror strstr strtol strtoull])

# Checks OS
dnl
dnl  check OS
dnl
AM_CONDITIONAL([LINUX], [test x"$(uname -s)" = xLinux])

# Checks CEFORE_DIR
dnl
dnl  check environment variable CEFORE_DIR
dnl
if test -z "$CEFORE_DIR"; then
  CEFORE_DIR_PATH=/usr/local/
else
  CEFORE_DIR_PATH=${CEFORE_DIR}/
fi
AC_SUBST(CEFORE_DIR_PATH)

echo "#define CEFORE_VERSION \"Cefore version $VERSION\"" > src/cefnetd/version.h

# Checks for header files(OpenSSL).
dnl
dnl  check OpenSSL library
dnl
if test "x${opssl_header_path}" = "x" ; then
  opssl_header_path=/usr/include/openssl/sha.h
else
  opssl_header_path=${opssl_header_path}/openssl/sha.h
fi
AC_CHECK_FILES([${opssl_header_path}],[],[AC_MSG_ERROR("OpenSSL is not installed.")])

# checks for args

dnl
dnl  check debug build
dnl
AC_ARG_ENABLE(
  debug,
  AS_HELP_STRING([--enable-debug], [debug build (default no)]),
  [enable_debug=yes],
  [enable_debug=no]
)
AM_CONDITIONAL(CEFDBG_ENABLE, test x"${enable_debug}" = xyes)

dnl
dnl  check csmgr
dnl
AC_ARG_ENABLE(
  csmgr,
  AS_HELP_STRING([--enable-csmgr], [content store manager (default no)]),
  [enable_csmgr=yes],
  [enable_csmgr=no]
)
AM_CONDITIONAL(CSMGR_ENABLE, test x"${enable_csmgr}" = xyes)

dnl
dnl  enable ccninfo
dnl
AM_CONDITIONAL(CCNINFO_ENABLE, true)

dnl
dnl  check cefping
dnl
AC_ARG_ENABLE(cefping,
  AS_HELP_STRING([--enable-cefping], [content ping tool (default no)]),
  [enable_cefping=yes],
  [enable_cefping=no]
)
AM_CONDITIONAL(CEFPING_ENABLE, test x"${enable_cefping}" = xyes)

dnl
dnl  check samptp
dnl
AC_ARG_ENABLE(
  samptp,
  AS_HELP_STRING([--enable-samptp], [sample transport (default no)]),
  [enable_samptp=yes],
  [enable_samptp=no]
)
AM_CONDITIONAL(SAMPTP_ENABLE, test x"${enable_samptp}" = xyes)

dnl
dnl  check NDN Plugin
dnl
AC_ARG_ENABLE(
  ndn,
  AS_HELP_STRING([--enable-ndn], [NDN Plugin (default no)]),
  [enable_ndn=yes],
  [enable_ndn=no]
)
AM_CONDITIONAL(NDNPLUGIN_ENABLE, test x"${enable_ndn}" = xyes)

dnl
dnl  check cache
dnl
AC_ARG_ENABLE(
  cache,
  AS_HELP_STRING([--enable-cache], [local cache on cefnetd (default no)]),
  [enable_cache=yes],
  [enable_cache=no]
)
AM_CONDITIONAL(CACHE_ENABLE, test x"${enable_cache}" = xyes)


AC_CONFIG_FILES([
  Makefile
  src/Makefile
  src/cefnetd/Makefile
  src/include/Makefile
  src/include/cefore/Makefile
  src/lib/Makefile
  src/plugin/Makefile
  src/bw_stat_plugin/Makefile
  utils/Makefile
  config/Makefile
  tools/Makefile
  tools/cefgetstream/Makefile
  tools/cefputstream/Makefile
  tools/cefgetfile/Makefile
  tools/cefputfile/Makefile
  tools/cefgetfile_sec/Makefile
  tools/cefputfile_sec/Makefile
  tools/cefgetchunk/Makefile
])

dnl
dnl  check csmgr
dnl
AM_COND_IF(
  [CSMGR_ENABLE],
  [AC_CONFIG_FILES([
      tools/csmgr/Makefile
      src/csmgrd/Makefile
      src/csmgrd/csmgrd/Makefile
      src/csmgrd/plugin/Makefile
      src/csmgrd/plugin/lib/Makefile
      src/csmgrd/plugin/lib/lru/Makefile
      src/csmgrd/plugin/lib/lfu/Makefile
      src/csmgrd/plugin/lib/fifo/Makefile
      src/csmgrd/lib/Makefile
      src/csmgrd/include/Makefile
      src/csmgrd/include/csmgrd/Makefile
  ])
])

dnl
dnl  check cefping
dnl
AM_COND_IF(
  [CEFPING_ENABLE],
  [AC_CONFIG_FILES([
    tools/cefping/Makefile
  ])
])

dnl
dnl  check ccninfo
dnl
AM_COND_IF(
  [CCNINFO_ENABLE],
  [AC_CONFIG_FILES([
    tools/ccninfo/Makefile
  ])
])

dnl
dnl  check cache
dnl
AM_COND_IF(
  [CACHE_ENABLE], 
  [AC_CONFIG_FILES([
  ])
])


AC_OUTPUT
