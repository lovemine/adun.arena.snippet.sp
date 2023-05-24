package adun.arena.spx.gist.lovemine;

//파일을 Open한 이후부터 갱신이 되었을 때 로직 처리

try {
    File file = new File("REMOTE.TXT");
    while (file.length() <= 0) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
     
    RandomAccessFile rFile = new RandomAccessFile(file, "r");
    rFile.seek(file.length());
     
    String line = null;
    while (true) {
        line = rFile.readLine();
        if (line == null || line.isEmpty()) {
            try {
                Thread.sleep(500);
                continue;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
 
        //로직 처리        
        doSomething();
    }
} catch (IOException e) {
    e.printStackTrace();
}