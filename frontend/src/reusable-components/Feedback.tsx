import { useRef } from "react";

type Props = {
    text: string,
    button: boolean
}

const Feedback = (props: Props) => {
    const {text, button} = {...props};

    const feedbackElement = useRef<HTMLDivElement>(null);


    const handleClose = () => {
        
        if (feedbackElement.current) {
            feedbackElement.current.remove();
        } 

    }

    return ( 
        <div className="feedback" ref={feedbackElement}>
        <p className="feedback__text">
            {text} 
        </p>
        {button && <button className="btn btn--smaller btn--greater-border-radius btn--margin" onClick={handleClose}>x</button>}
        </div>
     );
}
 
export default Feedback;