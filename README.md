We have implemented a system in JAVA in order to make the experiments and testing. We have created a server and a client and by using sockets we will transfer data between them. In our approach, the client will be the one that will define the number of threads that wants to be used for file transfer from client to server. This will indicate how many files will be send from client to server at the same time., of course if the number of files that will be sent is enough. In order to check this and to test the end of the file, we use the following formula:

pageNumber*concurrency + listOfFiles.length%concurrency

Together with the file the client sends to server also the file name so at the server side we will have an identical copy of the file. Also together with the file is sent its checksum so at the server-side, when the checksum is calculated, it will be compared with the checksum sent from client side and see if they are the same. If they are the same, it means that the information into the files have not been corrupted, otherwise, if the checksums value is not the same the server will request from the client to send again the specific file.

2.1 Calculation of the Checksum

The file is sent as a metadata together with the calculated checksum from Client to Server. First we put all the files that we want to transfer in a Queue and we create a thread for each file at the time that they are transferred. At server side we put the received files in a buffer and then calculate the file checksum and compare if this checksum is the same with the checksum that came with the file from client. We have serialized file metadata and i have allocated 4 bytes for megabytes in the buffer. We have created an HexaArray in order to convert the bytes into a hexa value for the checksum.
