import { Link } from "react-router-dom";
import { LOGOUT_URL } from "../../variable/constants";

type HeaderProps = {
    isAuthenticated: boolean,
    setIsAuthenticated: React.Dispatch<React.SetStateAction<boolean>>,
    setRenderLogoutText: React.Dispatch<React.SetStateAction<boolean>>;
}

const Header = (props: HeaderProps) => {

    const {isAuthenticated, setIsAuthenticated, setRenderLogoutText} = {...props} 

    const handleLogout = () => {
        fetch(LOGOUT_URL, {
            method: "POST",
            credentials: "include"
        })

        sessionStorage.clear();

        setIsAuthenticated(false);
        setRenderLogoutText(true);
    }

    return ( 
        <header className="header">
            <h1 className="header__title">Bookstore</h1>
            <nav>
                <ul className="header__navbar">
                    <li><Link to="/">Home</Link></li>
                    {!isAuthenticated && <li><Link to="/logIn">Log In</Link></li>}
                    {!isAuthenticated && <li><Link to="/register">Register</Link></li>}
                    {isAuthenticated && <li><Link to="/user/borrows">My Account</Link></li>}
                    {isAuthenticated && <li><Link to="/" onClick={handleLogout}>Log Out</Link></li>}   
                </ul>
            </nav>
        </header>
     );
}
 
export default Header;