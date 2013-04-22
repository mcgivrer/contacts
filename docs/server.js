var http       = require('http'),
    url        = require('url'),
    textile    = require('textile-js'),
    markdown   = require('marked'),
    fs         = require('fs'),
    filesys    = require('sys'),
    path       = require('path');

/**
 * overload String objecty with startsWith method.
 */
String.prototype.startsWith = function(prefix) {
    return this.indexOf(prefix) === 0;
}

/**
 * overload String object with endsWith method.
 */
String.prototype.endsWith = function(suffix) {
    return this.match(suffix+"$") == suffix;
};

/**
 * Configuration for markdown
 */
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

/**
 * Simple object for a web page.
 */
var Page = {
  title:      "Doc",
  content:    "",
  stylesheet: "css/doc.css",
  url:        "",
  filepath:   "",
  toHtml:function(){
    return  "<!DOCTYPE html >"
          + "<html>"
          + "<head>"
          + "<meta charset=\"UTF-8\">"
          + "<title>"+this.title+"</title>"
          + "<link rel=\"stylesheet\" type=\"text/css\" href=\""+this.stylesheet+"\" />"
          + "</head>"
          + "<body>" + this.content + "</body>"
          + "</html>";
  }
};

/**
 * Send a formated HTTP Server Error <code>errCode</code> with <code>message</code> to <code>response</code>.
 *
 * @param response The http response where to write error page.
 * @param errCode the HTTP Error code.
 * @param message the string for the error message.
 */
function sendHttpError(response, errCode,message){
  response.writeHeader(errCode, {"Content-Type": "text/html"});
  var err = Page;
  err.title="Server Error (code:"+errCode+")";
  err.content="<h1>Error</h1>\n<h2>Error "+errCode+"</h2>\n<p>"+message+"</p>";
  response.write(err.toHtml());      
  response.end();
  err=null;
}

/**
 * Render the <code>page</code> object instance on the 
 * <code>response</code> object in Markdown syntax parser.
 *
 * @param response The Http resposnse where to write page content.
 * @param page Page object to convert to HTML.
 */
function renderTextile(response, page){
  response.writeHeader(200, {"Content-Type": "text/html"});
  page.content = textile(page.content);
  response.write(page.toHtml(), "binary");
  response.end();  
}

/**
 * Render the <code>page</code> object instance on the 
 * <code>response</code> object in Texile syntax parser.
 *
 * @param response The Http resposnse where to write page content.
 * @param page Page object to convert to HTML.
 */
function renderMarkdown(response, page){
  response.writeHeader(200, {"Content-Type": "text/html"});
  page.content = markdown(page.content);
  response.write(page.toHtml(), "binary");  
  response.end();  
}

/** 
 * Send <code>data</code> from file <code>filepath</code> as 
 * binary content to <code>response</code>. if <code>filepath</code> 
 * contains ".css", set the Header mimes type to text/css.
 *
 * @param response the Http Response where to write the data.
 * @param filepath path to the file
 * @param data content data of the filepath file.
 */
function serveFile(response, page){
  if(page.filepath.endsWith(".css")){
    response.writeHeader(200, {"Content-Type": "text/css"});
  }
  response.write(page.content, "binary");
  response.end();  
}

/**
 * Http Server Request Handler
 * @param request 
 * @param response
 */
var ServeTextileDoc = function(request, response){
  var my_path = url.parse(request.url).pathname;  
  var full_path = path.join(process.cwd(),my_path);  
  fs.exists(full_path,function(exists){  
      if(!exists){

        sendHttpError(response,404,"Page for url: <code>"+my_path+"</code> does not exist !");0

      } else {
        fs.readFile(full_path, "binary", function(err, data){
          if(err) {

            sendHttpError(response, 500, "Server error while accessing url: <code>"+my_path+"</code> !");

          } else {  

            console.log("read file:"+full_path);
            response.writeHeader(200, {"Content-Type": "text/plain"});

            // Prepare page object to manipulate data.
            var page = Page;
            page.title="Doc";
            page.content=""+data;
            page.url = request.url;
            page.filepath =  full_path;
            
            if(full_path.endsWith(".md")){
              console.log("Serve Markdown text for page :"+ my_path)
              renderMarkdown(response, page, full_path, data);
  

            }else if(full_path.endsWith(".textile")){
              console.log("Serve Textile text for page :"+ my_path)
              renderTextile(response, page, full_path);

            }else{
              console.log("Serve simple file for :"+ my_path)

              serveFile(response,page);
            }

          }
        });              
      }
  });  
};

// Start the http server with this brand new handler.
var server = http.createServer(ServeTextileDoc).listen(8000);