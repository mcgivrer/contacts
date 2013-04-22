var http         = require('http'),
    url          = require('url'),
    textile      = require('textile-js'),
    marked       = require('marked'),
    highlight    = require('highlight'),
    mustache     = require('mustache'),
    fs           = require('fs'),
    filesys      = require('sys'),
    path         = require('path');

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
 * Configuration for marked
 */
marked.setOptions({
  gfm: true,
  tables: true,
  breaks: false,
  pedantic: false,
  sanitize: true,
  smartLists: true,
  langPrefix: 'lang-',
  highlight: function(code, lang) {
    if (lang === 'js') {
      return highlight.javascript(code);
    }
    /*else if (lang === 'xml') {
      return highlight.xml(code);
    }*/
    else if (lang === 'java') {
      return highlight.java(code);
    }
    return code;
  }
});

/**
 * Simple object for a web page.
 */
var Page = function(){
  /** Page title*/
  this.title       = "Doc";
  /** Content of the page */
  this.content     = "";
  /** stylesheet to use with this page */
  this.stylesheet  = "css/doc.css";
  this.url         = "";
  this.filepath    = "";
  // template Mustache to generate page.
  this.template    = "";

  /**
   * Generate CSS link.
   */
  this.includeCSS = function(){
    var cssListToHtml = "";
    this.stylesheet = (this.stylesheet.endsWith(",")?this.stylesheet:this.stylesheet+",")
    var csslist = this.stylesheet.split(",");
    for(css in csslist){
      if(csslist[css]!=""){
        cssListToHtml+=mustache.render("<link rel=\"stylesheet\" type=\"text/css\" href=\"{{css}}\" />",{"css":csslist[css]});;
      }
    }
    return cssListToHtml;
  }

  this.pageBase = function (page){
    return "<!DOCTYPE html >"
          + "<html>"
          + "<head>"
          + "<meta charset=\"UTF-8\">"
          + "<title>"+page.title+"</title>"
          + page.includeCSS()
          + "</head>"
          + "<body>" + page.content + "</body>"
          + "</html>";
  }
  /**
   * Create HTML page with all attributes.
   */
  this.toHtml = function(){
    var pageHTML =  this.pageBase(this);
     
    if(this.template!=""){

      var templatePath = path.join(process.cwd(),this.template); 

      fs.exists(templatePath,function(exists){

        fs.readFile(templatePath, "binary", function(err, data){

          // Template not found !

          if(err){

            console.log("Unable to read template:[" + templatePath + "]");
            this.content= "<style>p.error{color:red;background-color:#300;padding:10px;border:1px solid red;border-left:4px solid red;}</style>"
                         +"<p class=\"error\"/>Unable to read template page from "+templatePath+"!</p>";
            this.title="Http Server Error";
            this.template="";
            pageHTML = this.pageBase(this);
           
          // tempplate exists, try to generate page with Mustache. 

          }else{

            console.log("Use template:[file:" + templatePath + ", data:["+data+"]]");
            pageHTML = mustache.render(""+data, {"page": this} );
            console.log("generate:["+pageHTML+"]");
          }

        });
      });
    }
    return pageHTML;
  }
};

/**
 * Send a formated HTTP Server Error <code>errCode</code> with 
 * <code>message</code> to <code>response</code>.
 *
 * @param response The http response where to write error page.
 * @param errCode the HTTP Error code.
 * @param message the string for the error message.
 */
function sendHttpError(response, errCode,message){
  response.writeHeader(errCode, {"Content-Type": "text/html"});
  var err = new Page();
  err.template="/template.html"
  err.title="Server Error (code:"+errCode+")";
  err.content="<h1>Error</h1>\n<h2>Error "+errCode+"</h2>\n<p>"+message+"</p>";
  err.template="wiki/template.html";
  response.write(err.toHtml());      
  response.end();
  err=null;
}

/**
 * Render the <code>page</code> object instance on the 
 * <code>response</code> object in marked syntax parser.
 *
 * @param response The Http resposnse where to write page content.
 * @param page Page object to convert to HTML.
 */
function renderTextilePage(response, page){
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
function renderMarkdownPage(response, page){
  response.writeHeader(200, {"Content-Type": "text/html"});
  // retrieve first H1 title page.
  var treelex  = marked.lexer(page.content);
  page.title   = treelex[0].text;

  // Set the default template
  page.template = "/wiki/template.html";

  // convert to HTML content page.
  page.content = marked(page.content);

  // write page to response.
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
  var urlPath = url.parse(request.url).pathname;  
  if(urlPath=="/"){
    // redirect to the wiki Home page
    response.writeHead(302, {'Location': '/wiki/Home.md'});
    response.end();
  }else{
    var filePath = path.join(process.cwd(),urlPath);  
    fs.exists(filePath,function(exists){  
        if(!exists){

          sendHttpError(response,404,"Page for url: <code>"+urlPath+"</code> does not exist !");0

        } else {
          fs.readFile(filePath, "binary", function(err, data){
            if(err) {

              sendHttpError(response, 500, "Server error while accessing url: <code>"+urlPath+"</code> !");

            } else {  

              console.log("read file:"+filePath);
              response.writeHeader(200, {"Content-Type": "text/plain"});

              // Prepare page object to manipulate data.
              var page = new Page();
              page.title="Doc";
              page.content=""+data;
              page.url = request.url;
              page.filepath =  filePath;
              
              if(filePath.endsWith(".md")){

                console.log("Serve marked text for page :"+ urlPath)
                renderMarkdownPage(response, page, filePath, data);
    

              }else if(filePath.endsWith(".textile")){

                console.log("Serve Textile text for page :"+ urlPath)
                renderTextilePage(response, page, filePath);

              }else{

                console.log("Serve file for :"+ urlPath)
                serveFile(response,page);
              }

            }
          });              
        }
    });  
  }
};

// Start the http server with this brand new handler.
var server = http.createServer(ServeTextileDoc).listen(8000);