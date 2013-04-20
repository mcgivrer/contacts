var http    = require('http'),
    url     = require('url'),
    textile = require('textile-js'),
    markdown = require('marked'),
    fs      = require('fs'),
    filesys = require('sys'),
    path    = require('path'),
    string  = require('./string.js');


markdown.setOptions({
  gfm: true,
  tables: true,
  breaks: false,
  pedantic: false,
  sanitize: true,
  smartLists: true,
  langPrefix: 'lang-',
  highlight: null,
});

var Page = {
  title: "Doc",
  content: "",
  stylesheet: "css/doc.css",
  toHtml:function(){
    return  "<!DOCTYPE html >"
          + "<html>"
          + "<head>"
          + "<meta charset=\"UTF-8\">"
          + "<title>"+this.title+"</title>"
          + "<link rel=\"stylesheet\" type=\"text/css\" href=\""+this.stylesheet+"\" />"
          + "</head>"
          + "<body>"+this.content+"</body>"
          + "</html>";
  }
}
var ServeTextileDoc = function(request, response){
  var my_path = url.parse(request.url).pathname;  
    var full_path = path.join(process.cwd(),my_path);  
    fs.exists(full_path,function(exists){  
        if(!exists){  
            response.writeHeader(404, {"Content-Type": "text/plain"});
            response.write("404 Not Found\n");    
            response.end();
        } else {
              fs.readFile(full_path, "binary", function(err, data){
                   if(err) {    
                       response.writeHeader(500, {"Content-Type": "text/plain"});
                       response.write(err + "\n");    
                       response.end();
                   } else {  

                      console.log("read file:"+full_path);
                      response.writeHeader(200, {"Content-Type": "text/plain"});
                      var page = Page;
                      page.title="Doc";

                      page.content=""+data;
                      
                      if(full_path.endsWith(".md")){

                        response.writeHeader(200, {"Content-Type": "text/html"});
                        console.log("markdown data : "+data.substring(0,100));
                        page.content = markdown(page.content);
                        console.log("data markdown parsed : "+page.content.substring(0,100));
                        response.write(page.toHtml(), "binary");    

                      }else if(full_path.endsWith(".textile")){

                        response.writeHeader(200, {"Content-Type": "text/html"});
                        console.log("textile data : "+data.substring(0,100));
                        page.content = textile(page.content);
                        console.log("data textile parsed : "+page.content.substring(0,100));
                        response.write(page.toHtml(), "binary");    

                      }else{
                        if(full_path.endsWith(".css")){
                          response.writeHeader(200, {"Content-Type": "text/css"});
                        }
                        response.write(data, "binary");    

                      }

                      response.end();  
                  }
              });              
        }
    });  
};

var server = http.createServer(ServeTextileDoc).listen(8000);