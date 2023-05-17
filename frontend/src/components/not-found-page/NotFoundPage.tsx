import { Link } from "react-router-dom";
import notFoundImage from '../../images/reading.png';


const NotFoundPage = () => {
    return ( 
        <main className="not-found-page">
            <h2 className="not-found-page__title">Sorry</h2>
            <p className="not-found-page__paragraph">That page cannot be found</p>
            <img 
                        src={notFoundImage} 
                        alt="404 not found"
                        width="45"
                        height="45"
                        className="not-found-page__image"
            />
            <Link to="/" className="not-found-page__link">Back to the homepage...</Link>
        </main>
     );
}

export default NotFoundPage