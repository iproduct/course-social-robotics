from flask import Flask
import json
app = Flask(__name__)

@app.route("/")
def hello():
    return """
    <html>
    <head>
        <title>Flask Hallo World</title>
        <style>
            body {
                font-family: Helvetica, Arial, sans-serif;
                font-variant: small-caps;
            }
            .list {
                list-style-type: none;
            }
            .list li {
                display: inline-block;
                background-color: #2288ff;
                color: white;
                padding: 5px 10px;
                min-width: 100px;
                text-align: center;
            }
        </style>
    </head>
    <body>
        <h2>Hello from Flask!</h2>
        <h3 style="background-color:powderblue;">Things to learn:</h3>
        <p style="color: blue">Things is a paragraph.</h3>
        <ul class="list">
            <li>Add routes</li>
            <li>Create handler</li>
            <li>Start the server</li>
        </ul>
    </body>
    </html>
    """

@app.route("/layout")
def layout():
    return """
<!DOCTYPE html>
<html>
<head>
<style>
div.container {
    width: 100%;
    border: 1px solid gray;
}

header, footer {
    padding: 1em;
    color: white;
    background-color: black;
    clear: left;
    text-align: center;
}

nav {
    float: left;
    max-width: 160px;
    margin: 0;
    padding: 1em;
}

nav ul {
    list-style-type: none;
    padding: 0;
}
   
nav ul a {
    text-decoration: none;
}

article {
    margin-left: 170px;
    border-left: 1px solid gray;
    padding: 1em;
    overflow: hidden;
}
</style>
</head>
<body>

<div class="container">

<header>
   <h1>City Gallery</h1>
</header>
  
<nav>
  <ul>
    <li><a href="#">London</a></li>
    <li><a href="#">Paris</a></li>
    <li><a href="#">Tokyo</a></li>
  </ul>
</nav>

<article>
  <h1>London</h1>
  <p>London is the capital city of England. It is the most populous city in the  United Kingdom, with a metropolitan area of over 13 million inhabitants.</p>
  <p>Standing on the River Thames, London has been a major settlement for two millennia, its history going back to its founding by the Romans, who named it Londinium.</p>
</article>

<footer>Copyright &copy; W3Schools.com</footer>

</div>

</body>
</html>
    """

if __name__ == "__main__":
    app.run()
