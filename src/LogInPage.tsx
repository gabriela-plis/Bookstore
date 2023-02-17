import { Link } from "react-router-dom";

const LogInPage = () => {
    return ( 
        <div className="logInPage">
            <LogInSection />
            <RegisterOptionSection />
        </div>
     );
}

const LogInSection = () => {
    return (
        <section className="logIn">
            <h2 className="title">Log In to Your Account</h2>
            <form>
                <p className="form-fields">
                    <input type="number" placeholder="ID"/>
                    <input type="text" placeholder="Password"/>
                </p>
                <button className="btn logIn-btn">Log In</button>
            </form>
        </section>
    )
}

const RegisterOptionSection = () => {
    return (
        <section className="register">
            <h2 className="title">New to Bookstore?</h2>
            <p className="paragraph">Sing up and discover a great amount of new opportunities!</p>
            <Link to='/register'>SIGN UP</Link>
        </section>
    )
}
 
export default LogInPage;