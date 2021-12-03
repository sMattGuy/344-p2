To compile this program run
javac src/*.java -d bin

To run this program follow these steps
First, run the server:
java -cp bin Server

The server will then activate, and display an IP address and a port
    example:
        $ java -cp bin Server
        ServerSocket created on port 9371 with address 206.180.59.166

Next, run the ClientDriver:
java -cp bin ClientDriver address port
    example:
        java -cp bin ClientDriver 206.180.59.166 9371

Client driver will spawn every client necessary to run the program


!IMPORTANT!
ClientDriver will exit once every thread completes
Server WILL NOT TERMINATE ON ITS OWN!
Once ClientDriver terminates on its own, you can close Server with CTRL+C

This program requires two terminal windows to operate
If you have a Firewall, such as Comodo, you may be prompted to allow the program access

Flammia_ServerOutput.txt contains all output from Server
Flammia_ClientOutput.txt contains all output from ClientDriver

If you have any problems I am available 24/7
matthew.flammia71@qmail.cuny.edu