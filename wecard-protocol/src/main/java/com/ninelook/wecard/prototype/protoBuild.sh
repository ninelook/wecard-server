#!/bin/bash
PROTO_FILES=(
"apis/*.proto" 
"beans/*.proto"
"Communication.proto"
"Request.proto"
"Response.proto"
)

JAVA_SRC=../../../../
                                                                                

echo "delete protocol dir..."

rm -rf ../protocol
echo "delete done."

cmd=("protoc" "--java_out=${JAVA_SRC}" "${PROTO_FILES[@]}")

echo "SHELL:" ${cmd[@]}
${cmd[@]}

echo "ok!"