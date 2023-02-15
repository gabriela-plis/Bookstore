import { Link } from "react-router-dom";
import { BrowserRouter } from "react-router-dom";

const Header = () => {
    return ( 
        <header className="header">
            <h1 className="title">Bookstore</h1>
            <nav>
                <ul className="navbar">
                    <li><Link to="/">Home</Link></li>
                    <li><Link to="/">LogIn</Link></li>
                    <li><Link to="/">Register</Link></li>
                </ul>
            </nav>
        </header>
     );
}
 
export default Header;