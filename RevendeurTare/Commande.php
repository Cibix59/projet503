<?php
class Commande implements JsonSerializable
{
    private $qte;
    private $type;

    public static function fromJSON(string $CommandeJSON): Commande
    {
        $data = json_decode($CommandeJSON,true);
        $commTmp = new Commande($data["qte"],$data["type"]);
        return $commTmp;
    }

    public function jsonSerialize()
    {
        //todo : appeler jsonSerialize de la classe mere quand il y en aura une
        return [
            'qte' => $this->qte,
            'type' => $this->type
        ];
    }

    public function __construct(string $type,string $qte)
    {
        $this->qte = $qte;
        $this->type = $type;
    }
    public function getQte(): string
    {
        return $this->qte;
    }
    public static function getType(): string
    {
        return self::$type;
    }
    public function __toString(): string
    {
        return "Commande : type=$this->type; qte=" . $this->qte;
    }
}