import React from 'react';
import  './App.css';
import {Container , Row , Col , Button , Card , Accordion , Image , ButtonGroup , Form} from "react-bootstrap"
import 'bootstrap/dist/css/bootstrap.min.css';
import Salle from "./Components/Salle"
class App extends React.Component {
  constructor(props) {
    super(props);
   
    this.state = {
      Villes : {} , 
      SideBar : [] , 
      Salles : [] , 
      Cinemas : [] , 
    };
    //onClick={e => this.clicked(e, "id")}
  }
  async componentDidMount() {
   var villes =  await this.GetCities()
    this.setState({Villes : await this.GetCinemas(villes) })
    console.log(this.state.Villes)
 //  console.log(this.state.Villes[0].cinemas)
     await this.initSideBar()
  // console.log(this.state.Villes) 
  }
  async GetCities(){
    var res 
    await fetch('http://localhost:8080/villes').then(response => response.json())
      .then(responseJson => {
        res =  responseJson._embedded.villes
      }).catch(error => alert(error)); 
    //  console.log(this.state.Villes)
    return res 
  }

  async GetCinemas(villes){
   for (var i =0  ; i<villes.length ; i++){
    await fetch(villes[i]._links.cinemas.href).then(response => response.json())
      .then(responseJson => {
          villes[i].cinemas = responseJson._embedded.cinemas
      })
   }
    return villes
  }
 async init(){
  console.log("init")
    var villes = this.state.Villes
    var bar = []
    villes.forEach(element => {
    bar.push(<h4>{element.name}</h4>)
    })
    this.setState({sideBar : bar})
    console.log(this.state.sideBar.length)
  }
  async initSideBar(){
    
    var Villes = []
    var sideBar = [] 
    var x = 1
    this.state.Villes.forEach(element =>{
    var Cinemas = []
    element.cinemas.forEach(async cinema =>{
      //console.log(cinema._links.salles.href)
      Cinemas.push(<Button  id={""+cinema._links.salles.href} style={{marginBottom : 10}} className="Button" variant="info"  onClick={e => this.GetSalles(e, "id")}>{cinema.name}</Button>)
    }) 
      Villes.push(
      <Card>
        <Card.Header>
          <Accordion.Toggle id={100000} className="Button" as={Button} variant="primary" eventKey={x} >
            {element.name}
          </Accordion.Toggle>
        </Card.Header> 
        <Accordion.Collapse eventKey={x}>
      <Card.Body>
        {Cinemas}
      </Card.Body>
      </Accordion.Collapse>
      </Card>
        ) 
        x++
  })
  sideBar.push(<Accordion  defaultActiveKey="0" style={{ paddingLeft: 0, paddingRight: 0 }}>{Villes}</Accordion>)
  this.setState({SideBar : sideBar})  

}
 async  GetSalles(e){
  this.setState({Salles : []})
   var Salles = []
   console.log(e.target.id)
   await fetch(e.target.id).then(response => response.json())
  .then(responseJson => {
        responseJson._embedded.salles.forEach(salle =>{
          Salles.push(<Salle salle={salle}/>)
        })
  }).catch(error => alert(error)); 
  this.setState({Salles : Salles})
  }

  render() {
  return (
    
    <Container fluid >
      
    <Row style={{  }} >
    
      <Col xs={2} style={{ paddingTop : 10 ,paddingLeft: 10, paddingRight: 10 , alignItems : "centre"}}>
        <Card style={{borderBottom : "none"}}>
        <Card.Header> <h1 style={{textAlign : "center"}} >Villes</h1> </Card.Header>
        </Card>
        {this.state.SideBar}
      </Col >
      <Col xs={10} style={{ paddingLeft: 0, paddingRight: 10 , paddingTop : 10 }}>
        <Card style={{justifyContent : "center"  }}> 
          
          <Card.Header><h1 style={{textAlign : "center"}} >Salles</h1></Card.Header> 
          <Row style={{ margin:'1%' , justifyContent : "center" }}>
          {this.state.Salles}
          </Row>
        </Card>
      </Col>
    </Row>
   
  </Container>
  );
}
}

export default App;
