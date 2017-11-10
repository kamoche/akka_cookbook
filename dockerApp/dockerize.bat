echo "====================Removing target before making assembly================================"  
    
rd /s /q ..\target && del /q docker\*
echo "====================Entering to Hello-Akka directory and creating assembly================"

cd ..\ && sbt assembly
echo "====================Coming back to dockerApp directory===================================="  

cd dockerApp
echo "====================Copying application jar to docker folder=============================="

copy ..\target\scala-2.12\Hello-Akka-assembly-1.0.jar docker\

echo "====================Building docker image================================================="

docker build -t akkaapp

echo "====================Running docker container=============================================="

docker run akkaapp
