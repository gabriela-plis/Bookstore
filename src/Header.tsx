import { Link } from "react-router-dom";
import { BrowserRouter } from "react-router-dom";

const Header = () => {
    return ( 
        <header className="header">
            <h1 className="header__title">Bookstore</h1>
            <nav>
                <ul className="header__navbar">
                    <li><Link to="/">Home</Link></li>
                    <li><Link to="/logIn">Log In</Link></li>
                    <li><Link to="/register">Register</Link></li>
                </ul>
            </nav>
        </header>
     );
}
 
export default Header;