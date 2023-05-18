#!/usr/bin/env bash

#
# async-profiler usage details be found here https://github.com/jvm-profiling-tools/async-profiler
#

profile_graph=profile-graph.html

rm -rf $profile_graph

#
# possible options described here: https://github.com/jvm-profiling-tools/async-profiler/blob/v2.8.3/src/arguments.cpp#L52
#
async_profiler_options=-agentpath:/Users/mstepan/Desktop/async-profiler-2.8.3-macos/build/libasyncProfiler.so=start,file=$profile_graph,flamegraph,event=cpu
java_options="-XX:+UnlockDiagnosticVMOptions -XX:+DebugNonSafepoints $async_profiler_options"

java ${java_options} -jar target/app-java17-template-1.0.0-SNAPSHOT.jar 100000 || exit 1

open $profile_graph
